package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulationResumeDTO {
    private Long simulationId;
    private Long questionBankId;

    private Integer currentQuestionNumber;
    private Integer totalQuestions;

    private QuestionResponseDTO pendingQuestion;
}
