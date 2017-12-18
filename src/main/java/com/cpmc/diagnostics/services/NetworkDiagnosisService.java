package com.cpmc.diagnostics.services;

import com.cpmc.diagnostics.models.MachineConnectivityStatus;
import com.google.common.collect.EvictingQueue;
import nu.xom.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rakib on 8/23/2017.
 */
@Service
public class NetworkDiagnosisService {

    private static final int MAX_SIZE = 10;

    private Queue<MachineConnectivityStatus> queue;

    public NetworkDiagnosisService() {
        this.queue = EvictingQueue.create(MAX_SIZE);
//        enqueue();
    }

    public Queue<MachineConnectivityStatus> getQueue() {
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
                    MachineConnectivityStatus status = new MachineConnectivityStatus();

                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet httpGet = new HttpGet("http://uaf132943.ddns.uark.edu:10090/current");
                    HttpResponse httpResponse = client.execute(httpGet);
                    Builder parser = new Builder();
                    Document document = parser.build(httpResponse.getEntity().getContent());
                    XPathContext context = new XPathContext();
                    context.addNamespace("xmlns", "urn:mtconnect.org:MTConnectStreams:1.2");

                    // availability
                    Nodes machineAvailabilityNodes  = document.getRootElement().query(
                            "/xmlns:MTConnectStreams/xmlns:Streams/xmlns:DeviceStream/xmlns:ComponentStream/xmlns:Events/xmlns:Availability[@dataItemId='availability']",
                            context);
                    if (machineAvailabilityNodes.size() > 0) {
                        status.setMachineStatus(machineAvailabilityNodes.get(0).getValue());
                    }

                    Nodes operationStatusNodes  = document.getRootElement().query(
                            "/xmlns:MTConnectStreams/xmlns:Streams/xmlns:DeviceStream/xmlns:ComponentStream/xmlns:Events/xmlns:Printing[@dataItemId='startJobStatus']",
                            context);
                    if (operationStatusNodes.size() > 0) {
                        status.setOperationStatus(operationStatusNodes.get(0).getValue());
                    }

                    queue.add(status);

                    if ("UNAVAILABLE".equalsIgnoreCase(status.getMachineStatus())) {
                        // error
                    } else if ("AVAILABLE".equals(status.getMachineStatus()) &&
                            "REQUESTED".equals(status.getOperationStatus())) {
                        // warning
                    }

                } catch (IOException | ParsingException e) {
                    e.printStackTrace();
                }

            }
        }, 1000, 1000);
    }

    private boolean isWarning() {
        int warningConditionCount = 0;
        for (MachineConnectivityStatus status:this.getQueue()) {
            if ("AVAILABLE".equals(status.getMachineStatus()) &&
                    "REQUESTED".equals(status.getOperationStatus())) {
                ++warningConditionCount;
            }
        }
        return warningConditionCount >= 1;
    }

    private boolean isError() {
        int errorConditionCount = 0;
        for (MachineConnectivityStatus status:this.getQueue()) {
            if ("UNAVAILABLE".equals(status.getMachineStatus())) {
                ++errorConditionCount;
            }
        }
        return errorConditionCount >= 1;
    }

    public String getMachineDiagnostics() {
        if (isError()) {
            return "ERROR";
        } else {
            if (isWarning()) {
                return "WARNING";
            } else {
                return "OK";
            }
        }
    }

    public void cleanQueue() {
        this.getQueue().clear();
    }
}
