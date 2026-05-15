package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterApplicantRequestDTO {
    private String email;
    private String password;
    private String name;
    private LocalDate bornDate;
    private Long careerId;
    private String levelStudy;
    private String university;
}
