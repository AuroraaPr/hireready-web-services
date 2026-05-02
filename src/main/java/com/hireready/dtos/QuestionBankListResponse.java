package com.hireready.dtos;

import lombok.Data;

import java.util.List;

@Data
public class QuestionBankListResponse {

    private Long id;
    private String title;
    private String jobPosition;
    private String level;

    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED
    private List<String> actions;

    private String companyName;
    private Integer questionCount;

}
