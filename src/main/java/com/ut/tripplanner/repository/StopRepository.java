package com.ut.tripplanner.repository;

import com.ut.tripplanner.domain.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StopRepository extends JpaRepository<Stop, Long> {

    List<Stop> findStopByStopName(String stopName);
}
