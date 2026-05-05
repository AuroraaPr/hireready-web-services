package com.hireready.dtos;

import java.util.List;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateQuestionBankRequestDTO {

    private String name;
    private String description;
    private String jobPosition;
    private String level;

    private List<Long> careerIds;

    private List<QuestionRequestDTO> questions;
}
