package com.hireready.repositories;

import com.hireready.entities.Simulation;
import com.hireready.enums.SimulationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    public List<Simulation> findByApplicant_IdAndStatus(Long applicantId, SimulationStatus status);
    public Simulation findFirstByApplicant_IdAndStatusOrderByStartedAtDesc(Long applicantId, SimulationStatus status);
    public List<Simulation> findByApplicant_IdOrderByStartedAtDesc(Long applicantId);
    public List<Simulation> findByStartedAtIsNotNull();
    public Long countByApplicant_Id(Long applicantId);
    public Long countByApplicant_IdAndStatus(Long applicantId,SimulationStatus status);
    public List<Simulation> findByQuestionBank_Company_Id(Long companyId);
}