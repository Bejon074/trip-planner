package com.ut.tripplanner.model;

import com.ut.tripplanner.domain.Route;
import com.ut.tripplanner.domain.Stop;

public class NextStopDistance {
    private int timeDistance;
    private int waitingTime;
    private Route route;
    private int busArrivalTime;
    private Stop nextStop;

    public int getTimeDistance() {
        return timeDistance;
    }

    public void setTimeDistance(int timeDistance) {
        this.timeDistance = timeDistance;
    }

    public Stop getNextStop() {
        return nextStop;
    }

    public void setNextStop(Stop nextStop) {
        this.nextStop = nextStop;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getBusArrivalTime() {
        return busArrivalTime;
    }

    public void setBusArrivalTime(int busArrivalTime) {
        this.busArrivalTime = busArrivalTime;
    }

    @Override
    public String toString() {
        return "NextStopDistance{" +
                "timeDistance=" + timeDistance +
                ", waitingTime=" + waitingTime +
                ", route=" + route +
                ", busArrivalTime=" + busArrivalTime +
                ", nextStop=" + nextStop +
                '}';
    }
}
