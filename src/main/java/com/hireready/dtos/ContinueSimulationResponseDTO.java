package com.hireready.dtos;

import com.hireready.enums.SimulationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContinueSimulationResponseDTO {
    private Long simulationId;
    @Enumerated(EnumType.STRING)
    private SimulationStatus status;
    private QuestionResponseDTO pendingQuestion;  // null si todas estan completas
    private Integer totalQuestions;
    private Integer answeredQuestions;
}
