package com.hireready.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "simulation_reports")
public class SimulationReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer avgRelevance;
    private Integer avgClarity;
    private Integer avgStructure;
    private Integer overallScore;
    private Integer wordsPerMinute;

    @OneToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;
}
