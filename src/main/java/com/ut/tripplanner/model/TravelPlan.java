package com.ut.tripplanner.model;

import com.ut.tripplanner.domain.Location;

import java.util.Date;
import java.util.List;

public class TravelPlan {
    private Location departure;
    private Location arrival;
    private Date depTime;
    private List<TravelLeg> travelLegs;

    public Location getDeparture() {
        return departure;
    }

    public void setDeparture(Location departure) {
        this.departure = departure;
    }

    public Location getArrival() {
        return arrival;
    }

    public void setArrival(Location arrival) {
        this.arrival = arrival;
    }

    public Date getDepTime() {
        return depTime;
    }

    public void setDepTime(Date depTime) {
        this.depTime = depTime;
    }

    public List<TravelLeg> getTravelLegs() {
        return travelLegs;
    }

    public void setTravelLegs(List<TravelLeg> travelLegs) {
        this.travelLegs = travelLegs;
    }

    @Override
    public String toString() {
        return "TravelPlan{" +
                "departure=" + departure +
                ", arrival=" + arrival +
                ", depTime=" + depTime +
                ", travelLegs=" + travelLegs +
                '}';
    }
}
