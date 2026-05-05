package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionBankResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String jobPosition;
    private String level;
    private Long companyId;
    private String companyName;

    private List<Long> careerIds;

    private List<String> careerNames;

    private List<QuestionResponseDTO> questions;

}
