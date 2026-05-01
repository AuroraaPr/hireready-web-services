package com.hireready.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicantUpdateDTO {
        private String name;
        private LocalDate bornDate;
        private Long careerId;
        private String levelStudy;
        private String university;

}
