package com.ut.tripplanner.repository;

import com.ut.tripplanner.domain.Route;
import com.ut.tripplanner.domain.RouteStopMapping;
import com.ut.tripplanner.domain.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteStopMappingRepository extends JpaRepository<RouteStopMapping, Long> {

    List<RouteStopMapping> findByStop(Stop stop);
    RouteStopMapping findByRouteAndSequence(Route route, Integer seq);
    List<RouteStopMapping> findByRouteOrderBySequence(Route route);
}
