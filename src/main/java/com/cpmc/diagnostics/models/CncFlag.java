package com.cpmc.diagnostics.models;

/**
 * Created by Rakib on 12/17/2017.
 */
public class CncFlag {
    private String networkFlag;
    private String yAxesFlag;
    private String drillFlag;

    public CncFlag() {
        this.networkFlag = "OK";
        this.yAxesFlag = "OK";
        this.drillFlag = "OK";
    }

    public String getNetworkFlag() {
        return networkFlag;
    }

    public void setNetworkFlag(String networkFlag) {
        this.networkFlag = networkFlag;
    }

    public String getyAxesFlag() {
        return yAxesFlag;
    }

    public void setyAxesFlag(String yAxesFlag) {
        this.yAxesFlag = yAxesFlag;
    }

    public String getDrillFlag() {
        return drillFlag;
    }

    public void setDrillFlag(String drillFlag) {
        this.drillFlag = drillFlag;
    }
}
