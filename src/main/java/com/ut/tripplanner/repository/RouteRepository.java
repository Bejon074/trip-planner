package com.ut.tripplanner.repository;

import com.ut.tripplanner.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
