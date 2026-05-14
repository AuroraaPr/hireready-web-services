package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantSummaryResponseDTO {
    private Long applicantId;
    private Long userId;
    private String email;
    private String name;
    private LocalDate bornDate;
    private String careerName;
    private String levelStudy;
    private String university;
    private Boolean enabled;
    private Integer simulationCount;
}
