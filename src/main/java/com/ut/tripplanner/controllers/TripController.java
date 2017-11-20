package com.ut.tripplanner.controllers;

import com.ut.tripplanner.model.TravelLeg;
import com.ut.tripplanner.model.TravelPlan;
import com.ut.tripplanner.model.TravelPlanRequest;
import com.ut.tripplanner.services.TravelPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class TripController {

    @Autowired
    private TravelPlanService travelPlanService;

    @RequestMapping(value = "/get-travel-plan", method = RequestMethod.POST)
    public TravelPlan getTravelLeg(@RequestBody TravelPlanRequest travelPlanRequest) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        Date date = formatter.parse(travelPlanRequest.getDateString() + ":00");
        System.out.println(date);
        return travelPlanService.findTravelPlan(travelPlanRequest.getStartLat(),
                travelPlanRequest.getStartLong(),
                travelPlanRequest.getEndLat(),
                travelPlanRequest.getEndLong(),
                date);
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public void test() throws IOException{
        travelPlanService.insertIntoConnection();
    }
}
