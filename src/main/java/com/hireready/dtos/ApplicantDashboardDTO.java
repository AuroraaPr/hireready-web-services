package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//US22
@Data
public class ApplicantDashboardDTO {
    private Long totalSimulations;
    private Double averageScore;
    private Double bestScore;
    private List<ScorePointDTO> scoreEvolution;
    private List<FillerWordDTO> fillerWords;
}

@Data
@AllArgsConstructor
class ScorePointDTO {
    private String date;
    private Double score;
}

@Data
@AllArgsConstructor
class FillerWordDTO {
    private String word;
    private Integer count;
}