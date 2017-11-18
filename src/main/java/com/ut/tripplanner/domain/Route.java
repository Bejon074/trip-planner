package com.ut.tripplanner.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Route {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @Column
    private int number;

    @Column
    private String routeName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "routeStartId")
    private RouteStart routeStart;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public RouteStart getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(RouteStart routeStart) {
        this.routeStart = routeStart;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId=" + routeId +
                ", number=" + number +
                ", routeName='" + routeName + '\'' +
                ", routeStart=" + routeStart +
                '}';
    }
}
