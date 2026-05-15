package com.hireready.repositories;

import com.hireready.entities.Simulation;
import com.hireready.entities.SimulationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SimulationReportRepository extends JpaRepository<SimulationReport, Long> {
    public SimulationReport findBySimulation_Id(Long simulationId);
    public List<Simulation> findBySimulationStartedAtIsNotNull();
    @Query("SELECT AVG(sr.overallScore) FROM SimulationReport sr")
    public Double averageOverallScore();
    public List<SimulationReport> findBySimulation_Applicant_IdOrderBySimulation_CompletedAtAsc(Long applicantId);
}
