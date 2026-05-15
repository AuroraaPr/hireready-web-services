package com.hireready.dtos;

import com.hireready.enums.SimulationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExitSimulationResponseDTO {
    private Long simulationId;
    private SimulationStatus status;
    private Integer totalQuestions;
    private Integer answeredQuestions;
    private String message;
}
