package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDashboardResponseDTO {
    private Long totalBanks;
    private Long totalSimulations;
    private List<CountByLabelDTO> simulationsPerBank;
    private List<CountByLabelDTO> topUsedBanks;
    private List<String> unusedBanks;
    private Integer averageOverallScore;
    private Integer averageRelevance;
    private Integer averageClarity;
    private Integer averageStructure;
    private List<QuestionScoreDTO> lowestScoredQuestions;
    private List<CountByLabelDTO> applicantsByCareer;
    private List<CountByLabelDTO> applicantsByLevelStudy;
    private List<CountByLabelDTO> topUniversities;
    private Boolean hasEnoughData;
    private String message;
}
