package com.hireready.services;

import com.hireready.dtos.SubmitResponseRequestDTO;
import com.hireready.entities.Response;
import com.hireready.entities.Simulation;

import java.util.List;

public interface ResponseService {
    public Response submit(Simulation simulation, SubmitResponseRequestDTO request);
    public List<Response> listBySimulationId(Long simulationId);
}
