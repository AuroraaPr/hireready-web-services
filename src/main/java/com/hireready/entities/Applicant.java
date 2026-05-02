package com.hireready.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "applicants")
@Data
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String level_study;
    private String university;
    private LocalDate bornDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}