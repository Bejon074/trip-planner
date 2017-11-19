package com.ut.tripplanner.services;

import com.ut.tripplanner.domain.RouteStartTime;
import com.ut.tripplanner.domain.RouteStopMapping;
import com.ut.tripplanner.domain.Stop;
import com.ut.tripplanner.domain.StopConnection;
import com.ut.tripplanner.enums.ScheduleDay;
import com.ut.tripplanner.model.PreviousNodeDetails;
import com.ut.tripplanner.repository.RouteStopMappingRepository;
import com.ut.tripplanner.repository.StopConnectionRepository;
import com.ut.tripplanner.repository.StopRepository;
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
    public void findTravelPlan(String startStopName, String endStopName, Date date) {
        List<Stop> startStops = stopRepository.findStopByStopName(startStopName);
        List<Stop> endStops = stopRepository.findStopByStopName(endStopName);

        if (startStops.size() < 1 || endStops.size() < 1) {
            return;
        }

        ScheduleDay scheduleDay = Libs.getScheduleDay(date);
        int timeInHourAndMinute = Libs.getTimeInHourMinuteFormat(date);

        List<Map<Stop, PreviousNodeDetails>> costingMapList = new ArrayList<>();
        for (Stop startStop : startStops) {
            for (Stop endStop : endStops) {
                Map<Stop, PreviousNodeDetails> costingMap = findShortestPath(startStop,
                        endStop, scheduleDay, timeInHourAndMinute);
                costingMapList.add(costingMap);
            }
        }
    }

    private Map<Stop, PreviousNodeDetails> findShortestPath(Stop startStop,
                                                            Stop endStop,
                                                            ScheduleDay scheduleDay,
                                                            int timeInHourAndMinute) {

        Map<Stop, PreviousNodeDetails> costMap = new HashMap<>();
        List<Stop> allStops = stopRepository.findAll();
        for (Stop stop : allStops) {
            if (stop.equals(startStop)) {
                PreviousNodeDetails previousNodeDetails = new PreviousNodeDetails();
                previousNodeDetails.setCost(timeInHourAndMinute);
                costMap.put(stop, previousNodeDetails);
            } else {
                PreviousNodeDetails previousNodeDetails = new PreviousNodeDetails();
                previousNodeDetails.setCost(Integer.MAX_VALUE);
                costMap.put(stop, previousNodeDetails);
            }
        }
        Set<Stop> unvisitedNodes = new HashSet<>();
        for (Stop stop : allStops) {
            unvisitedNodes.add(stop);
        }

        while (!unvisitedNodes.isEmpty()) {
            Stop lowestCostStop = takeSmallestCostStopFromUnvisited(unvisitedNodes, costMap);
            unvisitedNodes.remove(lowestCostStop);
            if (lowestCostStop.equals(endStop)) {
                break;
            }
            calculateDistanceOfNextStops(lowestCostStop, scheduleDay, costMap.get(lowestCostStop).getCost(), costMap);
        }
        return costMap;
    }

    private Stop takeSmallestCostStopFromUnvisited(Set<Stop> unvisited,
                                                   Map<Stop, PreviousNodeDetails> map) {
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
                                              Map<Stop, PreviousNodeDetails> costMap) {

        List<RouteStopMapping> belongsToRoutes = routeStopMappingRepository.findByStop(stop);
        if (belongsToRoutes.size() < 1) {
            return ;
        }
        for (RouteStopMapping routeStopMapping : belongsToRoutes) {
            List<RouteStopMapping> nextStopMappingList = routeStopMappingRepository
                    .findByRouteOrderBySequence(routeStopMapping.getRoute());
            boolean isNextFound = false;
            int totalDistanceFromRouteStartStop = 0;
            boolean ignoreConnection = true;
            Stop previousStop = new Stop();
            for (RouteStopMapping nextStopMapping : nextStopMappingList) {
                StopConnection stopConnection=null;
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
                            PreviousNodeDetails previousNodeDetails = costMap.get(nextStopMapping.getStop());
                            previousNodeDetails.setCost(Libs.addTime(routeStartTime.getTime(), totalDistanceFromRouteStartStop));
                            previousNodeDetails.setRoute(nextStopMapping.getRoute());
                            previousNodeDetails.setTravelTime(stopConnection.getConnectionDuration());
                            previousNodeDetails.setStop(stop);
                            break;
                        }
                    }
                }
                if(isNextFound){
                    break;
                }
                if (nextStopMapping.getStop().equals(stop)) {
                    isNextFound = true;
                }else{
                    isNextFound = false;
                }
                ignoreConnection = false;
                previousStop = nextStopMapping.getStop();
            }
        }
    }

}
