package com.hireready.repositories;

import com.hireready.entities.Simulation;
import com.hireready.enums.SimulationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    List<Simulation> findByApplicant_IdAndStatus(Long applicantId, SimulationStatus status);
    Simulation findFirstByApplicant_IdAndStatusOrderByStartedAtDesc(Long applicantId, SimulationStatus status);

    // US-22
    @Query("SELECT COUNT(s), AVG(s.score), MAX(s.score) FROM Simulation s WHERE s.applicant.id = :applicantId")
    Object[] getApplicantBasicMetrics(Long applicantId);
}