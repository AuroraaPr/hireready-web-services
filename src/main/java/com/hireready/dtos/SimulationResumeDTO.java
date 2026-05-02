package com.hireready.dtos;

import lombok.Data;

@Data
public class SimulationResumeDTO {
    private Long simulationId;
    private Long questionBankId;

    private Integer currentQuestionNumber;
    private Integer totalQuestions;

    private QuestionDTO pendingQuestion;
}
