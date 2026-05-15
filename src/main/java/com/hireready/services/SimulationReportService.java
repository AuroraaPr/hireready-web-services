package com.hireready.services;

import com.hireready.entities.Response;
import com.hireready.entities.Simulation;
import com.hireready.entities.SimulationReport;

import java.util.List;

public interface SimulationReportService {
    public SimulationReport generate(Simulation simulation, List<Response> responses);
    public SimulationReport findBySimulationId(Long simulationId);
}
