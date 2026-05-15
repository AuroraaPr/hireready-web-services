package com.hireready.repositories;

import com.hireready.entities.FillerWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FillerWordRepository extends JpaRepository<FillerWord, Long> {
    public List<FillerWord> findBySimulation_Id(Long simulationId);
    public List<FillerWord> findBySimulation_Applicant_Id(Long applicantId);
}
