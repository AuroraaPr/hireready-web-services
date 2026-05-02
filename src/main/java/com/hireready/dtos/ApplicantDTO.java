package com.hireready.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicantDTO {
    private String name;
    private String email;
    private String password;
    private String level_study;
    private String university;
    private LocalDate bornDate;
}
