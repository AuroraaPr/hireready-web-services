package com.hireready.services;

import com.hireready.entities.Response;

import java.util.List;

public interface ResponseService {
    public List<Response> findBySimulationId(Long simulationId);
}
