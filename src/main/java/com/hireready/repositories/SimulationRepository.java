package com.hireready.repositories;

import com.hireready.entities.Simulation;
import com.hireready.enums.SimulationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    public List<Simulation> findByApplicant_IdAndStatus(Long applicantId, SimulationStatus status);
    public Simulation findFirstByApplicant_IdAndStatusOrderByStartedAtDesc(Long applicantId, SimulationStatus status);
    public List<Simulation> findByApplicant_IdOrderByStartedAtDesc(Long applicantId);
    public List<Simulation> findByStartedAtIsNotNull();
    public Long countByApplicant_Id(Long applicantId);
    public Long countByApplicant_IdAndStatus(Long applicantId,SimulationStatus status);
    public List<Simulation> findByQuestionBank_Company_Id(Long companyId);
    public Long countByApplicant_IdAndStartedAtAfter(Long applicantId, LocalDateTime cutoff);
    public Long countByApplicant_IdAndStatusAndCompletedAtAfter(Long applicantId, SimulationStatus status, LocalDateTime cutoff);
    public List<Simulation> findByQuestionBank_Company_IdAndStartedAtAfter(Long companyId, LocalDateTime cutoff);
    public List<Simulation> findByStartedAtAfter(LocalDateTime cutoff);
    public List<Simulation> findByApplicant_IdAndQuestionBank_IdAndStatus(
            Long applicantId, Long questionBankId, SimulationStatus status);
    public Simulation findFirstByApplicant_IdAndQuestionBank_IdAndStatusOrderByStartedAtDesc(
            Long applicantId, Long questionBankId, SimulationStatus status);
}