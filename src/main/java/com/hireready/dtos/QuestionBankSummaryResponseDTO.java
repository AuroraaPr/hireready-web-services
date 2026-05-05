package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBankSummaryResponseDTO {
    private Long id;
    private String name;
    private String companyName;
    private String description;
    private String jobPosition;
    private String level;
    private List<String> careerNames;
    private Integer numQuestions;
    private String status;
}
