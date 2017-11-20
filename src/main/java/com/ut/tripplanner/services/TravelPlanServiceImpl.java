package com.ut.tripplanner.services;

import com.ut.tripplanner.domain.RouteStartTime;
import com.ut.tripplanner.domain.RouteStopMapping;
import com.ut.tripplanner.domain.Stop;
import com.ut.tripplanner.domain.StopConnection;
import com.ut.tripplanner.enums.ScheduleDay;
import com.ut.tripplanner.enums.Transport;
import com.ut.tripplanner.model.PathDetails;
import com.ut.tripplanner.model.TravelLeg;
import com.ut.tripplanner.model.TravelPlan;
import com.ut.tripplanner.repository.RouteStopMappingRepository;
import com.ut.tripplanner.repository.StopConnectionRepository;
import com.ut.tripplanner.repository.StopRepository;
import com.ut.tripplanner.utils.Constants;
import com.ut.tripplanner.utils.Libs;
import com.ut.tripplanner.utils.SortByTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TravelPlanServiceImpl implements TravelPlanService {

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private StopConnectionRepository stopConnectionRepository;

    @Autowired
    private RouteStopMappingRepository routeStopMappingRepository;

    @Override
    public TravelPlan findTravelPlan(double startLat, double startLong, double endLat, double endLong, Date date) {

        Stop startStop = new Stop();
        startStop.setStopName("startStop");
        startStop.setStopId(-1L);
        startStop.setxCoord(startLat);
        startStop.setyCoord(startLong);


        Stop endStop = new Stop();
        endStop.setStopName("endStop");
        endStop.setStopId(-2L);
        endStop.setxCoord(endLat);
        endStop.setyCoord(endLong);

        ScheduleDay scheduleDay = Libs.getScheduleDay(date);
        int timeInHourAndMinute = Libs.getTimeInHourMinuteFormat(date);

        Map<Stop, PathDetails> costingMap = findShortestPath(startStop, endStop, scheduleDay, timeInHourAndMinute);
        TravelPlan travelPlan = generateTravelPlan(costingMap, startStop, endStop, date);
        return travelPlan;
    }


    private Map<Stop, PathDetails> findShortestPath(Stop startStop,
                                                    Stop endStop,
                                                    ScheduleDay scheduleDay,
                                                    int timeInHourAndMinute) {

        Map<Stop, PathDetails> costMap = new HashMap<>();
        List<Stop> allStops = stopRepository.findAll();
        allStops.add(startStop);
        allStops.add(endStop);
        for (Stop stop : allStops) {
            if (stop.equals(startStop)) {
                PathDetails pathDetails = new PathDetails();
                pathDetails.setCost(timeInHourAndMinute);
                costMap.put(stop, pathDetails);
            } else {
                PathDetails pathDetails = new PathDetails();
                pathDetails.setCost(Integer.MAX_VALUE);
                costMap.put(stop, pathDetails);
            }
        }
        Set<Stop> unvisitedStops = new HashSet<>();
        for (Stop stop : allStops) {
            unvisitedStops.add(stop);
        }

        while (!unvisitedStops.isEmpty()) {
            Stop lowestCostStop = takeSmallestCostStopFromUnvisited(unvisitedStops, costMap);
            unvisitedStops.remove(lowestCostStop);
            if (lowestCostStop.equals(endStop)) {
                break;
            }
            calculateDistanceOfNextStops(lowestCostStop, scheduleDay, costMap.get(lowestCostStop).getCost(), costMap);
            calculateWalkingDistances(unvisitedStops, costMap, lowestCostStop);
        }
        return costMap;
    }

    private Stop takeSmallestCostStopFromUnvisited(Set<Stop> unvisited,
                                                   Map<Stop, PathDetails> map) {
        int smallestDistance = Integer.MAX_VALUE;
        Stop smallestDistanceStop = new Stop();
        for (Stop stop : unvisited) {
            if (map.containsKey(stop) && map.get(stop).getCost() <= smallestDistance) {
                smallestDistanceStop = stop;
                smallestDistance = map.get(stop).getCost();
            }
        }
        return smallestDistanceStop;
    }

    private void calculateDistanceOfNextStops(Stop stop, ScheduleDay scheduleDay,
                                              int currentTime,
                                              Map<Stop, PathDetails> costMap) {

        List<RouteStopMapping> belongsToRoutes = routeStopMappingRepository.findByStop(stop);
        if (belongsToRoutes.size() < 1) {
            return;
        }
        for (RouteStopMapping routeStopMapping : belongsToRoutes) {
            List<RouteStopMapping> nextStopMappingList = routeStopMappingRepository
                    .findByRouteOrderBySequence(routeStopMapping.getRoute());
            boolean isNextFound = false;
            int totalDistanceFromRouteStartStop = 0;
            boolean ignoreConnection = true;
            Stop previousStop = new Stop();
            for (RouteStopMapping nextStopMapping : nextStopMappingList) {
                StopConnection stopConnection = null;
                if (!ignoreConnection) {
                    stopConnection = stopConnectionRepository
                            .findByPreviousStopAndNextStop(previousStop, nextStopMapping.getStop());
                    if (stopConnection != null) {
                        totalDistanceFromRouteStartStop += stopConnection.getConnectionDuration();
                    }
                }
                if (isNextFound && totalDistanceFromRouteStartStop != 0) {
                    List<RouteStartTime> routeStartTimes = nextStopMapping.getRoute().getRouteStart().getStartTimes();
                    Collections.sort(routeStartTimes, new SortByTime());
                    for (RouteStartTime routeStartTime : routeStartTimes) {
                        if (scheduleDay == routeStartTime.getScheduleDay() && currentTime <= Libs.addTime(routeStartTime.getTime(),
                                totalDistanceFromRouteStartStop - stopConnection.getConnectionDuration())
                                && Libs.addTime(routeStartTime.getTime(), totalDistanceFromRouteStartStop) < costMap.get(nextStopMapping.getStop()).getCost()) {
                            PathDetails pathDetails = costMap.get(nextStopMapping.getStop());
                            pathDetails.setCost(Libs.addTime(routeStartTime.getTime(), totalDistanceFromRouteStartStop));
                            pathDetails.setRoute(nextStopMapping.getRoute());
                            pathDetails.setTravelTime(stopConnection.getConnectionDuration());
                            pathDetails.setTransport(Transport.BY_BUS);
                            pathDetails.setPreviousStop(stop);
                            break;
                        }
                    }
                }
                if (isNextFound) {
                    break;
                }
                if (nextStopMapping.getStop().equals(stop)) {
                    isNextFound = true;
                } else {
                    isNextFound = false;
                }
                ignoreConnection = false;
                previousStop = nextStopMapping.getStop();
            }
        }
    }

    private void calculateWalkingDistances(Set<Stop> unvisitedStops, Map<Stop, PathDetails> costMap, Stop lowestCostStop) {

        for (Stop unvisited : unvisitedStops) {
            double distanceBetweenLowest = Libs.distance(lowestCostStop.getxCoord(), lowestCostStop.getyCoord(),
                    unvisited.getxCoord(), unvisited.getyCoord());
            int requiredTime = (int) (distanceBetweenLowest / Constants.walkingSpeed);
            if (Libs.addTime(costMap.get(lowestCostStop).getCost(), requiredTime) < costMap.get(unvisited).getCost()) {
                PathDetails pathDetails = costMap.get(unvisited);
                pathDetails.setTransport(Transport.ON_FOOT);
                pathDetails.setCost(Libs.addTime(costMap.get(lowestCostStop).getCost(), requiredTime));
                pathDetails.setPreviousStop(lowestCostStop);
                pathDetails.setTravelTime(requiredTime);
            }
        }
    }

    private TravelPlan generateTravelPlan(Map<Stop, PathDetails> costMap, Stop startStop, Stop endStop, Date date) {

        TravelPlan travelPlan = new TravelPlan();
        travelPlan.setArrival(endStop);
        travelPlan.setDeparture(startStop);
        travelPlan.setDepTime(date);
        Stop accStop = endStop;
        List<TravelLeg> allLegs = new ArrayList<>();
        while (!accStop.equals(startStop)) {
            PathDetails pathDetails = costMap.get(accStop);
            TravelLeg travelLeg = new TravelLeg();
            travelLeg.setDuration(pathDetails.getTravelTime());
            travelLeg.setTransport(pathDetails.getTransport());
            travelLeg.setStartStop(pathDetails.getPreviousStop());
            travelLeg.setEndStop(accStop);
            travelLeg.setPath(pathDetails.getPreviousStop().getStopName() + " - " + accStop.getStopName());
            travelLeg.setRouteNumber(pathDetails.getRoute() != null ? pathDetails.getRoute().getNumber() : -1);
            travelLeg.setEndTime(pathDetails.getCost());
            travelLeg.setStartTime(Libs.substractTime(pathDetails.getCost(), pathDetails.getTravelTime()));
            allLegs.add(travelLeg);
            accStop = pathDetails.getPreviousStop();
        }
        List<TravelLeg> shorterLegs = new ArrayList<>();
        if(allLegs.size() == 1){
            shorterLegs.add(allLegs.get(0));
        }else {
            int accRouteNumber = allLegs.get(allLegs.size() - 1).getRouteNumber();
            TravelLeg accTravelLeg = allLegs.get(allLegs.size() - 1);
            for (int index = allLegs.size() - 2; index >= 0; index--) {
                TravelLeg travelLeg = allLegs.get(index);
                if (accRouteNumber == travelLeg.getRouteNumber() && index != 0) {
                    accTravelLeg.setEndStop(travelLeg.getEndStop());
                    accTravelLeg.setPath(accTravelLeg.getPath() + " ; " + travelLeg.getPath());
                    accTravelLeg.setDuration(travelLeg.getDuration() + accTravelLeg.getDuration());
                    accTravelLeg.setEndTime(travelLeg.getEndTime());
                } else if (accRouteNumber == travelLeg.getRouteNumber() && index == 0) {
                    accTravelLeg.setEndStop(travelLeg.getEndStop());
                    accTravelLeg.setPath(accTravelLeg.getPath() + " ; " + travelLeg.getPath());
                    accTravelLeg.setDuration(travelLeg.getDuration() + accTravelLeg.getDuration());
                    accTravelLeg.setEndTime(travelLeg.getEndTime());
                    shorterLegs.add(accTravelLeg);
                } else if (accRouteNumber != travelLeg.getRouteNumber() && index != 0) {
                    shorterLegs.add(accTravelLeg);
                    accTravelLeg = allLegs.get(index);
                    accRouteNumber = accTravelLeg.getRouteNumber();
                } else {
                    shorterLegs.add(accTravelLeg);
                    if (!isConsists(allLegs.get(0), shorterLegs)) {
                        shorterLegs.add(allLegs.get(0));
                    }
                }
            }
        }

        travelPlan.setTravelLegs(shorterLegs);
        return travelPlan;
    }

    private boolean isConsists(TravelLeg travelLeg, List<TravelLeg> travelLegs){
        for(TravelLeg travelLeg1: travelLegs){
            if(travelLeg1.equals(travelLeg)){
                return true;
            }
        }
        return false;
    }

}
