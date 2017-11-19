package com.ut.tripplanner.controllers;

import com.ut.tripplanner.model.TravelLeg;
import com.ut.tripplanner.model.TravelPlan;
import com.ut.tripplanner.services.TravelPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@Controller
public class TripController {

    @Autowired
    private TravelPlanService travelPlanService;

    @RequestMapping(value = "/get-travel-plan", method = RequestMethod.POST)
    @ResponseBody
    public TravelLeg getTravelLeg(@RequestBody TravelPlan travelPlan) {
        TravelLeg travelLeg = new TravelLeg();
        travelLeg.setMessage("Nothing to show");
        return travelLeg;
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public void test() throws IOException{
        travelPlanService.findTravelPlan("A", "H", new Date());
    }
}
