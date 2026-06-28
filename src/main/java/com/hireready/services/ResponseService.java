package com.hireready.services;

import com.hireready.entities.Response;
import com.hireready.entities.Simulation;

import java.util.List;

public interface ResponseService {
    public Response submit(Simulation simulation, Long questionId, Integer duration,
                           String transcription, byte[] audioBytes, String contentType);
    public List<Response> listBySimulationId(Long simulationId);
    public boolean existsInSimulation(Long responseId, Long simulationId);
}
