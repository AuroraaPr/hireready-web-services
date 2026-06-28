package com.hireready.services;

import com.hireready.entities.Response;
import com.hireready.entities.ResponseAnalysis;

public interface ResponseAnalysisService {
    public ResponseAnalysis analyzeAndSave(Response response);
    public void analyzeAsync(Response response);
    public ResponseAnalysis findByResponseId(Long responseId);
}
