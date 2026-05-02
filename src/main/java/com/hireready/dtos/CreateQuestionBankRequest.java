package com.hireready.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestionBankRequest {

    private String title;
    private String jobPosition;
    private String level;

    private List<Long> careerIds;

    private List<QuestionRequest> questions;

    //getter setters
}
