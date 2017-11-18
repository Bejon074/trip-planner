package com.ut.tripplanner.services;

import com.ut.tripplanner.domain.RouteStartTime;

import java.util.Comparator;

public class SortByTime implements Comparator<RouteStartTime> {

    @Override
    public int compare(RouteStartTime o1, RouteStartTime o2) {
        return o1.getTime() - o2.getTime();
    }
}
