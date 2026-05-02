package com.hireready.dtos;

import com.hireready.enums.SimulationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimulationResponseDTO {
    private Long simulationId;
    private Long questionBankId;
    private SimulationStatus status;
    private LocalDateTime startedAt;
}
