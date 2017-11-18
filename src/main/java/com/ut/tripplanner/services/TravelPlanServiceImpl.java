package com.ut.tripplanner.services;

import com.ut.tripplanner.domain.RouteStartTime;
import com.ut.tripplanner.domain.RouteStopMapping;
import com.ut.tripplanner.domain.Stop;
import com.ut.tripplanner.domain.StopConnection;
import com.ut.tripplanner.enums.ScheduleDay;
import com.ut.tripplanner.model.NextStopDistance;
import com.ut.tripplanner.model.PreviousNodeDetails;
import com.ut.tripplanner.repository.RouteStopMappingRepository;
import com.ut.tripplanner.repository.StopConnectionRepository;
import com.ut.tripplanner.repository.StopRepository;
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ScheduleDay scheduleDay = ScheduleDay.values()[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        String minute = calendar.get(Calendar.MINUTE) < 10 ? "0"+calendar.get(Calendar.MINUTE) : ""+calendar.get(Calendar.MINUTE);
        int timeInHourAndMinute = Integer.parseInt("" + calendar.get(Calendar.HOUR_OF_DAY) + minute);
        List<Map<Stop, PreviousNodeDetails>> costingMapList = new ArrayList<>();
        for (Stop startStop : startStops) {
            for (Stop endStop : endStops) {
                Map<Stop, PreviousNodeDetails> costingMap = findShortestPath(startStop, endStop, scheduleDay, timeInHourAndMinute);
                costingMapList.add(costingMap);
            }
        }
    }

    private Map<Stop, PreviousNodeDetails> findShortestPath(Stop startStop, Stop endStop, ScheduleDay scheduleDay, int timeInHourAndMinute) {

        Map<Stop, PreviousNodeDetails> costingMap = new HashMap<>();
        List<Stop> allStops = stopRepository.findAll();
        for (Stop stop : allStops) {
            if (stop.equals(startStop)) {
                PreviousNodeDetails previousNodeDetails = new PreviousNodeDetails();
                previousNodeDetails.setCost(timeInHourAndMinute);
                costingMap.put(stop, previousNodeDetails);
            } else {
                PreviousNodeDetails previousNodeDetails = new PreviousNodeDetails();
                previousNodeDetails.setCost(Integer.MAX_VALUE);
                costingMap.put(stop, previousNodeDetails);
            }
        }
        Set<Stop> unvisitedNodes = new HashSet<>();
        for (Stop stop : allStops) {
            unvisitedNodes.add(stop);
        }
        Set<Stop> visitedStops = new HashSet<>();

        while (!unvisitedNodes.isEmpty()) {
            Stop smalestStop = takeSmallestCostStopFromUnvisited(unvisitedNodes, costingMap);
            if (smalestStop.equals(endStop)) {
                break;
            }
            findSmallestDistanceNextStop(smalestStop, scheduleDay, costingMap.get(smalestStop).getCost(), costingMap);
            unvisitedNodes.remove(smalestStop);
            visitedStops.add(smalestStop);
        }
        return costingMap;
    }

    private Stop takeSmallestCostStopFromUnvisited(Set<Stop> unvisited, Map<Stop, PreviousNodeDetails> map) {
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

    private void findSmallestDistanceNextStop(Stop stop, ScheduleDay scheduleDay, int currentTime, Map<Stop, PreviousNodeDetails> stopMap) {

        List<RouteStopMapping> routeStopMappings = routeStopMappingRepository.findByStop(stop);
        if (routeStopMappings.size() < 1) {
            return ;
        }
        for (RouteStopMapping routeStopMapping : routeStopMappings) {
            List<RouteStopMapping> nextNodeMappingList = routeStopMappingRepository.findByRouteOrderBySequence(routeStopMapping.getRoute());
            boolean check = false;
            int localTimeDistance = 0;
            boolean isFirst = true;
            Stop previousStop = new Stop();
            for (RouteStopMapping routeStopMapping1 : nextNodeMappingList) {
                if (!isFirst) {
                    StopConnection stopConnection = stopConnectionRepository.findByPreviousStopAndNextStop(previousStop, routeStopMapping1.getStop());
                    System.out.println("database inconsistency 1");
                    if (stopConnection != null) {
                        localTimeDistance += stopConnection.getConnectionDuration();
                    }
                }
                if (check && localTimeDistance != 0) {
                    List<RouteStartTime> routeStartTimes = routeStopMapping1.getRoute().getRouteStart().getStartTimes();
                    Collections.sort(routeStartTimes, new SortByTime());
                    for (RouteStartTime routeStartTime : routeStartTimes) {
                        if (scheduleDay == routeStartTime.getScheduleDay() && currentTime < sum(routeStartTime.getTime(), localTimeDistance)
                                && stopMap.get(previousStop).getCost() + localTimeDistance < stopMap.get(routeStopMapping1.getStop()).getCost()) {
                            PreviousNodeDetails previousNodeDetails = stopMap.get(routeStopMapping1.getStop());
                            previousNodeDetails.setCost(sum(routeStartTime.getTime(), localTimeDistance));
                            previousNodeDetails.setRoute(routeStopMapping1.getRoute());
                            previousNodeDetails.setStop(previousStop);
                            check = false;
                        }
                    }
                }
                if (routeStopMapping.getStop().equals(stop)) {
                    check = true;
                }
                isFirst = false;
                previousStop = routeStopMapping1.getStop();
            }
        }
    }

    private int sum(int formatedTime, int plainTime) {
        System.out.println("formated time: " + formatedTime + " plaintime: " + plainTime);
        int formatedTime2 = plainTime;
        if (plainTime >= 60) {
            formatedTime2 = (plainTime / 60) * 100 + plainTime % 60;
        }
        int remainderOne = formatedTime % 100;
        int remainderTwo = formatedTime2 % 100;
        if (remainderOne + remainderTwo >= 60) {
            int returnValue = (formatedTime / 100 + formatedTime2 / 100 + (remainderOne + remainderTwo) / 60) * 100 + (remainderOne + remainderTwo) % 60;
            System.out.println("returnValue: " + returnValue);
            return returnValue;
        } else {
            System.out.println("returnValue: " + formatedTime + formatedTime2);
            return formatedTime + formatedTime2;
        }
    }

}
