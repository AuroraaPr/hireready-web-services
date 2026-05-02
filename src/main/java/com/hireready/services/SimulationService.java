package com.hireready.services;

import com.hireready.dtos.SimulationResponseDTO;
import com.hireready.dtos.SimulationResumeDTO;
import com.hireready.dtos.SimulationStartRequestDTO;
import com.hireready.entities.Simulation;

public interface SimulationService {
    public SimulationResponseDTO startSimulation(Long applicantId, SimulationStartRequestDTO simulationStartRequestDTO);
    public SimulationResumeDTO getActiveSimulationState(Long applicantId);
}
