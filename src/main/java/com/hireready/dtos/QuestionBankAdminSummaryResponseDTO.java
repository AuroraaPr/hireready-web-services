package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBankAdminSummaryResponseDTO {
    private Long id;
    private String name;
    private Long companyId;
    private String companyName;
    private Boolean companyEnabled;
    private String jobPosition;
    private String level;
    private List<String> careerNames;
    private Integer numQuestions;
    private Integer numSimulations;
}
