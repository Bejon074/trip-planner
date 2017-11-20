package com.ut.tripplanner.model;

import com.ut.tripplanner.enums.ScheduleDay;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TravelPlanRequest {

    private double startLat;
    private double startLong;
    private double endLat;
    private double endLong;
    private String dateString;

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLong() {
        return startLong;
    }

    public void setStartLong(double startLong) {
        this.startLong = startLong;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLong() {
        return endLong;
    }

    public void setEndLong(double endLong) {
        this.endLong = endLong;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public String toString() {
        return "TravelPlanRequest{" +
                "startLat=" + startLat +
                ", startLong=" + startLong +
                ", endLat=" + endLat +
                ", endLong=" + endLong +
                ", date=" + dateString +
                '}';
    }
}
