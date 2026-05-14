package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDashboardResponseDTO {
    private Long totalSimulations;
    private Long completedSimulations;
    private Integer averageOverallScore;
    private Integer bestOverallScore;
    private Integer averageRelevance;
    private Integer averageClarity;
    private Integer averageStructure;
    private List<ScoreTimePointDTO> scoreOverTime;
    private List<FillerWordResponseDTO> topFillerWords;
    private Boolean hasEnoughData;
    private String message;
}
