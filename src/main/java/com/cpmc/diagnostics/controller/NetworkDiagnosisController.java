package com.cpmc.diagnostics.controller;

import com.cpmc.diagnostics.services.NetworkDiagnosisService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Rakib on 5/25/2017.
 */
@Controller
public class NetworkDiagnosisController {

    private NetworkDiagnosisService networkDiagnosisService;

    private static int counter = 0;
    private static final String VIEW_INDEX = "index";
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(NetworkDiagnosisController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody public String welcome() {
        return networkDiagnosisService.getMachineDiagnostics();
    }

    private NetworkDiagnosisService getNetworkDiagnosisService() {
        return networkDiagnosisService;
    }

    @Autowired
    private void setNetworkDiagnosisService(NetworkDiagnosisService networkDiagnosisService) {
        this.networkDiagnosisService = networkDiagnosisService;
    }
}