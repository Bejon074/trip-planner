package com.ut.tripplanner.repository;

import com.ut.tripplanner.domain.Stop;
import com.ut.tripplanner.domain.StopConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StopConnectionRepository extends JpaRepository<StopConnection, Long> {

    List<StopConnection> findByPreviousStop(Stop previousStop);
    StopConnection findByPreviousStopAndNextStop(Stop previousStop, Stop nextStop);
}
