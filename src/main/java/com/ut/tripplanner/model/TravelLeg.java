package com.ut.tripplanner.model;

import com.ut.tripplanner.domain.Location;
import com.ut.tripplanner.domain.Stop;
import com.ut.tripplanner.enums.Transport;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class TravelLeg {

    private int duration;
    @Enumerated(EnumType.STRING)
    private Transport transport;
    private Stop startStop;
    private Stop endStop;
    private String path;
    private int routeNumber;
    private int startTime;
    private int endTime;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Stop getStartStop() {
        return startStop;
    }

    public void setStartStop(Stop startStop) {
        this.startStop = startStop;
    }

    public Stop getEndStop() {
        return endStop;
    }

    public void setEndStop(Stop endStop) {
        this.endStop = endStop;
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TravelLeg{" +
                "duration=" + duration +
                ", transport=" + transport +
                ", startStop=" + startStop +
                ", endStop=" + endStop +
                ", path='" + path + '\'' +
                ", routeNumber=" + routeNumber +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelLeg)) return false;

        TravelLeg travelLeg = (TravelLeg) o;

        if (getDuration() != travelLeg.getDuration()) return false;
        if (getRouteNumber() != travelLeg.getRouteNumber()) return false;
        if (getStartTime() != travelLeg.getStartTime()) return false;
        if (getEndTime() != travelLeg.getEndTime()) return false;
        if (getTransport() != travelLeg.getTransport()) return false;
        if (!getStartStop().equals(travelLeg.getStartStop())) return false;
        if (!getEndStop().equals(travelLeg.getEndStop())) return false;
        return getPath().equals(travelLeg.getPath());
    }

    @Override
    public int hashCode() {
        int result = getDuration();
        result = 31 * result + getTransport().hashCode();
        result = 31 * result + getStartStop().hashCode();
        result = 31 * result + getEndStop().hashCode();
        result = 31 * result + getPath().hashCode();
        result = 31 * result + getRouteNumber();
        result = 31 * result + getStartTime();
        result = 31 * result + getEndTime();
        return result;
    }
}
