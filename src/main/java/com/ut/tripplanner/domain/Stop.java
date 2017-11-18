package com.ut.tripplanner.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Stop extends Location {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stopId;

    @Column
    private String stopName;

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "stopId=" + stopId +
                ", stopName='" + stopName + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop)) return false;

        Stop stop = (Stop) o;

        if (!getStopId().equals(stop.getStopId())) return false;
        return getStopName().equals(stop.getStopName());
    }

    @Override
    public int hashCode() {
        int result = getStopId().hashCode();
        result = 31 * result + getStopName().hashCode();
        return result;
    }
}
