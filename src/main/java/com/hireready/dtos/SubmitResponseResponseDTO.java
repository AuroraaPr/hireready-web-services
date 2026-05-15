package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmitResponseResponseDTO {
    private Long simulationId;
    private Long savedResponseId;
    private QuestionResponseDTO nextQuestion; // si la respondida era la ultima >> null
    private Integer totalQuestions;
    private Integer answeredQuestions;
    private Boolean isLast; // respondida ultima >> true
}
