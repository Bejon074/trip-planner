package com.ut.tripplanner.services;

import com.ut.tripplanner.model.TravelPlan;

import java.util.Date;

public interface TravelPlanService {

    TravelPlan findTravelPlan(double startLat, double startLong, double endLat, double endLong, Date date);
    void insertIntoConnection();
}
