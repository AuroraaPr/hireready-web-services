package com.hireready.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "responses")
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String audioUrl;
    private String transcription;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToOne(mappedBy = "response", fetch = FetchType.EAGER)
    private ResponseAnalysis responseAnalysis;
}
