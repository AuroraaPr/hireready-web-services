package com.hireready.repositories;

import com.hireready.entities.SimulationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SimulationReportRepository extends JpaRepository<SimulationReport, Long> {
    public SimulationReport findBySimulation_Id(Long simulationId);
    @Query("SELECT AVG(sr.overallScore) FROM SimulationReport sr")
    public Double averageOverallScore();
    public List<SimulationReport> findBySimulation_Applicant_IdOrderBySimulation_CompletedAtAsc(Long applicantId);
    public List<SimulationReport> findBySimulation_Applicant_IdAndSimulation_CompletedAtAfter(
            Long applicantId, LocalDateTime cutoff);
    public List<SimulationReport> findTop10BySimulation_Applicant_IdOrderBySimulation_CompletedAtDesc(Long applicantId);
    @Query("SELECT AVG(sr.overallScore) FROM SimulationReport sr WHERE sr.simulation.completedAt >= ?1")
    public Double averageOverallScoreSince(LocalDateTime cutoff);
}
