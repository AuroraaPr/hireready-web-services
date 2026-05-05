package com.hireready.services;

import com.hireready.dtos.ContinueSimulationResponseDTO;
import com.hireready.dtos.SimulationResponseDTO;
import com.hireready.dtos.SimulationResumeDTO;
import com.hireready.dtos.SimulationStartRequestDTO;

public interface SimulationService {
    SimulationResponseDTO start(Long applicantUserId, SimulationStartRequestDTO simulationStartRequestDTO);
    ContinueSimulationResponseDTO continueLatest(Long applicantUserId);
}
