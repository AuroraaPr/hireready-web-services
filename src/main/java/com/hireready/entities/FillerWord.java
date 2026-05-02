package com.hireready.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "filler_words")
public class FillerWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;
}
