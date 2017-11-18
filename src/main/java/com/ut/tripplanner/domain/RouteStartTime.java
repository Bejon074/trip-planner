package com.ut.tripplanner.domain;

import com.ut.tripplanner.enums.ScheduleDay;

import javax.persistence.*;

@Entity
public class RouteStartTime {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeStartTimeId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "routeStartId")
    private RouteStart routeStart;

    @Enumerated(value = EnumType.ORDINAL)
    @Column
    private ScheduleDay scheduleDay;

    @Column
    private int time;

    public Long getRouteStartTimeId() {
        return routeStartTimeId;
    }

    public void setRouteStartTimeId(Long routeStartTimeId) {
        this.routeStartTimeId = routeStartTimeId;
    }

    public RouteStart getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(RouteStart routeStart) {
        this.routeStart = routeStart;
    }

    public ScheduleDay getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(ScheduleDay scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RouteStartTime{" +
                "routeStartTimeId=" + routeStartTimeId +
                ", routeStart=" + routeStart +
                ", scheduleDay=" + scheduleDay +
                ", time=" + time +
                '}';
    }
}
