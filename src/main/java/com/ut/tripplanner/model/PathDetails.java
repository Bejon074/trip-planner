package com.ut.tripplanner.model;

import com.ut.tripplanner.domain.Route;
import com.ut.tripplanner.domain.Stop;
import com.ut.tripplanner.enums.Transport;

public class PathDetails {

    private int cost;
    private Stop previousStop;
    private Route route;
    private int travelTime;
    private Transport transport;

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Stop getPreviousStop() {
        return previousStop;
    }

    public void setPreviousStop(Stop previousStop) {
        this.previousStop = previousStop;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "PathDetails{" +
                "cost=" + cost +
                ", previousStop=" + previousStop +
                ", route=" + route +
                ", travelTime=" + travelTime +
                ", transport=" + transport +
                '}';
    }
}
