package com.hireready.repositories;

import com.hireready.entities.ResponseAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseAnalysisRepository extends JpaRepository<ResponseAnalysis, Long> {
    public ResponseAnalysis findByResponse_Id(Long responseId);
}
