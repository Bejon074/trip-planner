package com.ut.tripplanner.services;

import java.util.Date;

public interface TravelPlanService {

    void findTravelPlan(String startStopName, String endStopName, Date date);
}
