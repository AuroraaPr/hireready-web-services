package com.hireready.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "simulations")
public class Simulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String status;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "question_bank_id")
    QuestionBank questionBank;

    @OneToMany(mappedBy = "simulation", fetch = FetchType.EAGER)
    private List<Response> responses;

    @OneToMany(mappedBy = "simulation", fetch = FetchType.EAGER)
    private List<FillerWord> fillerWords;

    @OneToOne(mappedBy = "simulation", fetch = FetchType.EAGER)
    private SimulationReport simulationReport;

}
