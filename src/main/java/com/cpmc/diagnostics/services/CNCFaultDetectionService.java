package com.cpmc.diagnostics.services;

import com.cpmc.diagnostics.models.CNCStatus;
import com.cpmc.diagnostics.models.CncFlag;
import com.google.common.collect.EvictingQueue;
import nu.xom.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rakib on 12/17/2017.
 */
public class CNCFaultDetectionService {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(CNCFaultDetectionService.class);


    private static final int MAX_SIZE = 10;
    private static final String FLAG_OK = "OK";
    private static final String FLAG_WARNING = "WARNING";
    private static final String FLAG_ERROR = "ERROR";

    private CncFlag flag = new CncFlag();

    // machine specific variables
    private static final float maxDiff = 50.0f;
    private static final float lowestDrillVibration = 0.0f;

    private Queue<CNCStatus> queue;

    public CNCFaultDetectionService() {
        this.queue = EvictingQueue.create(MAX_SIZE);
        enqueue();
    }

    public Queue<CNCStatus> getQueue() {
        return queue;
    }

    private void enqueue() {
        // timer function
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // code for fetching status data
                try {
                    CNCStatus status = new CNCStatus();

                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet httpGet = new HttpGet("http://uaf132943.ddns.uark.edu:1080/current");
                    HttpResponse httpResponse = client.execute(httpGet);
                    Builder parser = new Builder();
                    Document document = parser.build(httpResponse.getEntity().getContent());
                    XPathContext context = new XPathContext();
                    context.addNamespace("xmlns", "urn:mtconnect.org:MTConnectStreams:1.2");
                    // get availability value
                    Nodes machineAvailabilityNodes  = document.getRootElement().query(
                            "/xmlns:MTConnectStreams/xmlns:Streams/xmlns:DeviceStream/xmlns:ComponentStream" +
                                    "/xmlns:Events/xmlns:Availability[@dataItemId='availability']", context);
                    if (machineAvailabilityNodes.size() > 0) {
                        status.setAvailability(machineAvailabilityNodes.get(0).getValue());
                    }
                    // get y-axes value
                    Nodes yPositionNodes  = document.getRootElement().query(
                            "/xmlns:MTConnectStreams/xmlns:Streams/xmlns:DeviceStream/xmlns:ComponentStream" +
                                    "/xmlns:Samples/xmlns:Position[@dataItemId='yPos']", context);
                    if (yPositionNodes.size() > 0) {
                        status.setyPosition(yPositionNodes.get(0).getValue());
                    }
                    // get availability value
                    Nodes actualYPositionNodes  = document.getRootElement().query(
                            "/xmlns:MTConnectStreams/xmlns:Streams/xmlns:DeviceStream/xmlns:ComponentStream" +
                                    "/xmlns:Samples/xmlns:Position[@dataItemId='yPosActual']", context);
                    if (actualYPositionNodes.size() > 0) {
                        status.setActualYPosition(actualYPositionNodes.get(0).getValue());
                    }
                    // get availability value
                    Nodes drillVibration  = document.getRootElement().query(
                            "/xmlns:MTConnectStreams/xmlns:Streams/xmlns:DeviceStream/xmlns:ComponentStream" +
                                    "/xmlns:Samples/xmlns:Position[@dataItemId='drillVibr']", context);
                    if (drillVibration.size() > 0) {
                        status.setDrillVibration(drillVibration.get(0).getValue());
                    }

                    queue.add(status);

                    // now generate the flag variable using the data saved in the queue
                    flagAnalysis();
                    System.out.println();
                    logger.debug("Network="+flag.getNetworkFlag() + ", Y-Axes=" + flag.getyAxesFlag()+ ", Drill="
                            + flag.getDrillFlag());

                } catch (IOException | ParsingException e) {
                    e.printStackTrace();
                }

            }
        }, 2000, 2000);
    }

    private synchronized void flagAnalysis() throws IOException {
        // network flag analysis
        setNetworkFlag();
        // axes flag analysis
        setyAxesFlag();
        // drill flag analysis
        setDrillFlag();
    }

    private void setNetworkFlag() {
        flag.setNetworkFlag(FLAG_OK);
    }

    private void setyAxesFlag() throws IOException {
        float avgY = 0.0f;
        float avgActY = 0.0f;
        for (CNCStatus cncStatus:queue) {
            avgY += Float.valueOf(cncStatus.getyPosition());
            avgActY += Float.valueOf(cncStatus.getActualYPosition());
        }
        avgY /= queue.size();
        avgActY /= queue.size();

        float diff = Math.abs((avgY - avgActY));

        if (diff >= 50.0f ) {
            flag.setyAxesFlag(FLAG_WARNING);
            // FIXME: TEST IT
            stopCncMachine();
        } else {
            flag.setyAxesFlag(FLAG_OK);
        }
    }

    private void setDrillFlag() {
        float avgDrill = 0.0f;
        String availability = "";
        for (CNCStatus cncStatus: queue) {
            avgDrill += Float.valueOf(cncStatus.getDrillVibration());
            availability = cncStatus.getAvailability();
        }
        avgDrill /= queue.size();
        flag.setDrillFlag(avgDrill <= 0.0f && "RUN".equals(availability)? FLAG_ERROR : FLAG_OK);
    }

    public CncFlag getFlag() {
        return flag;
    }

    private boolean stopCncMachine() throws IOException {
        String operationXML = "{deviceId: \"CNC\", operationId: \"stop\", parameters: []}";
        String URL = "http://uaf132854.ddns.uark.edu:9002/virtualization-uark/operate/device";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URL);
        post.setEntity(new StringEntity(operationXML));

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }
}
