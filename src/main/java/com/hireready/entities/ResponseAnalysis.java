package com.hireready.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "response_analyses")
public class ResponseAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer relevanceScore;
    private Integer clarityScore;
    private Integer structureScore;
    @Column(columnDefinition = "TEXT")
    private String feedback;

    @OneToOne
    @JoinColumn(name = "response_id")
    private Response response;
}
