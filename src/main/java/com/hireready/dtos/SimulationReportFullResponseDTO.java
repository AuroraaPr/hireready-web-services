package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationReportFullResponseDTO {
    private Long simulationId;
    private String bankName;
    private String companyName;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer overallScore;
    private Integer avgRelevance;
    private Integer avgClarity;
    private Integer avgStructure;
    private Integer wordsPerMinute;
    private List<FillerWordResponseDTO> fillerWords;
    private List<ResponseDetailResponseDTO> responses;
}
