package com.hireready.services;

import com.hireready.dtos.ContinueSimulationResponseDTO;
import com.hireready.dtos.SimulationResponseDTO;
import com.hireready.dtos.SimulationStartRequestDTO;

public interface SimulationService {
    public SimulationResponseDTO start(Long applicantUserId, SimulationStartRequestDTO simulationStartRequestDTO);
    public ContinueSimulationResponseDTO continueLatest(Long applicantUserId);
}
