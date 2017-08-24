package com.cpmc.diagnostics.models;

/**
 * Created by Rakib on 8/23/2017.
 */
public class MachineConnectivityStatus {

    private String machineStatus;
    private String operationStatus;

    public MachineConnectivityStatus() {
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }
}
