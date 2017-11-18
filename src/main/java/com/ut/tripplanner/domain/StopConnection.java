package com.ut.tripplanner.domain;

import javax.persistence.*;

@Entity
public class StopConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long stopConnectionId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "previousStopId", referencedColumnName = "stopId")
    private Stop previousStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nextStopId", referencedColumnName = "stopId")
    private Stop nextStop;

    @Column
    private int connectionDuration;

    public Long getStopConnectionId() {
        return stopConnectionId;
    }

    public void setStopConnectionId(Long stopConnectionId) {
        this.stopConnectionId = stopConnectionId;
    }

    public Stop getPreviousStop() {
        return previousStop;
    }

    public void setPreviousStop(Stop previousStop) {
        this.previousStop = previousStop;
    }

    public Stop getNextStop() {
        return nextStop;
    }

    public void setNextStop(Stop nextStop) {
        this.nextStop = nextStop;
    }

    public int getConnectionDuration() {
        return connectionDuration;
    }

    public void setConnectionDuration(int connectionDuration) {
        this.connectionDuration = connectionDuration;
    }

    @Override
    public String toString() {
        return "StopConnection{" +
                "stopConnectionId=" + stopConnectionId +
                ", previousStop=" + previousStop +
                ", nextStop=" + nextStop +
                ", connectionDuration=" + connectionDuration +
                '}';
    }
}
