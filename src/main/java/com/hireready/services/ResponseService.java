package com.hireready.services;

import com.hireready.entities.Response;

import java.util.List;

public interface ResponseService {
    List<Response> findBySimulationId(Long simulationId);
}
