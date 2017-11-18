package com.ut.tripplanner.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class RouteStart {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeStartId;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
            ,mappedBy = "routeStart")
    private List<RouteStartTime> startTimes;

    public Long getRouteStartId() {
        return routeStartId;
    }

    public void setRouteStartId(Long routeStartId) {
        this.routeStartId = routeStartId;
    }

    public List<RouteStartTime> getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(List<RouteStartTime> startTimes) {
        this.startTimes = startTimes;
    }

    @Override
    public String toString() {
        return "RouteStart{" +
                "routeStartId=" + routeStartId +
                '}';
    }
}
