package com.ut.tripplanner.model;

import com.ut.tripplanner.domain.Route;
import com.ut.tripplanner.domain.Stop;

public class PreviousNodeDetails {

    private int cost;
    private Stop stop;
    private Route route;
    private int travelTime;

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
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

    @Override
    public String toString() {
        return "PreviousNodeDetails{" +
                "cost=" + cost +
                ", stop=" + stop +
                ", route=" + route +
                ", travelTime=" + travelTime +
                '}';
    }
}
