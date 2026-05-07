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

    // US-23: Obtener métricas base para la empresa vinculando a través del banco de preguntas
    @Query("SELECT COUNT(s), AVG(s.score) FROM Simulation s JOIN s.questionBank q WHERE q.company.id = :companyId")
    Object[] getCompanyBasicMetrics(Long companyId);

    // US-23: Obtener distribución de postulantes por carrera para una empresa
    @Query("SELECT s.applicant.career, COUNT(s) FROM Simulation s JOIN s.questionBank q WHERE q.company.id = :companyId GROUP BY s.applicant.career")
    List<Object[]> getCareerDistributionByCompany(Long companyId);
}