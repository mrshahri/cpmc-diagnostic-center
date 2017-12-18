package com.cpmc.diagnostics.controller;

import com.cpmc.diagnostics.models.CncFlag;
import com.cpmc.diagnostics.services.CNCFaultDetectionService;
import com.cpmc.diagnostics.services.NetworkDiagnosisService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Rakib on 5/25/2017.
 */
@Controller
public class DiagnosisController {

    private NetworkDiagnosisService networkDiagnosisService;
    private CNCFaultDetectionService cncFaultDetectionService;

    private static int counter = 0;
    private static final String VIEW_INDEX = "cnc-diagnostics-center";
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DiagnosisController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody public String getDiagnosisCheck() {
        return networkDiagnosisService.getMachineDiagnostics();
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public Response clearDiagnosisBuffer() {
        networkDiagnosisService.cleanQueue();
        return Response.ok().build();
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getDiagnosisGUI(ModelMap model) {
        model.addAttribute("flagUrl", "http://10.5.54.29:8100/app-diagnostics-center/cnc-flags");
        return VIEW_INDEX;
    }

    @RequestMapping(value = "/cnc-flags", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    @ResponseBody public CncFlag getCncFlags() {
        CncFlag flag = cncFaultDetectionService.getFlag();
        return flag;
    }

    private NetworkDiagnosisService getNetworkDiagnosisService() {
        return networkDiagnosisService;
    }

    @Autowired
    private void setNetworkDiagnosisService(NetworkDiagnosisService networkDiagnosisService) {
        this.networkDiagnosisService = networkDiagnosisService;
    }

    @Autowired
    private void setCncFaultDetectionService(CNCFaultDetectionService cncFaultDetectionService) {
        this.cncFaultDetectionService = cncFaultDetectionService;
    }
}