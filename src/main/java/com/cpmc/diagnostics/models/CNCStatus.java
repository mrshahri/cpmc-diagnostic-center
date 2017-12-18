package com.cpmc.diagnostics.models;

/**
 * Created by Rakib on 12/17/2017.
 */
public class CNCStatus {
    private String availability;
    private String yPosition;
    private String actualYPosition;
    private String drillVibration;

    public CNCStatus() {
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getyPosition() {
        return yPosition;
    }

    public void setyPosition(String yPosition) {
        this.yPosition = yPosition;
    }

    public String getActualYPosition() {
        return actualYPosition;
    }

    public void setActualYPosition(String actualYPosition) {
        this.actualYPosition = actualYPosition;
    }

    public String getDrillVibration() {
        return drillVibration;
    }

    public void setDrillVibration(String drillVibration) {
        this.drillVibration = drillVibration;
    }
}
