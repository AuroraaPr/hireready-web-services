package com.hireready.repositories;

import com.hireready.entities.Simulation;
import com.hireready.enums.SimulationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    List<Simulation> findByApplicant_IdAndStatus(Long applicantId, SimulationStatus status);
    Simulation findFirstByApplicant_IdAndStatusOrderByStartedAtDesc(Long applicantId, SimulationStatus status);
}