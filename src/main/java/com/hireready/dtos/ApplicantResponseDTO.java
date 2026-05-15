package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicantResponseDTO {
    private Long applicantId;
    private Long userId;
    private String email;
    private String name;
    private LocalDate bornDate;
    private Long careerId;
    private String careerName;
    private String levelStudy;
    private String university;
}
