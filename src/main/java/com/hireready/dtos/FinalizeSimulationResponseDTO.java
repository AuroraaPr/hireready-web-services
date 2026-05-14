package com.hireready.dtos;

import com.hireready.enums.SimulationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalizeSimulationResponseDTO {
    private Long simulationId;
    private SimulationStatus status;
    private LocalDateTime completedAt;
    private Integer overallScore;
}
