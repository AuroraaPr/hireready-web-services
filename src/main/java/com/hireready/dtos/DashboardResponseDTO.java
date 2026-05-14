package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDTO {
    private Long totalUsers;
    private Long totalApplicants;
    private Long totalCompanies;
    private Long totalSimulations;
    private List<TimeBucketDTO> simulationsOverTime;
    private List<CountByLabelDTO> applicantsByCareer;
    private List<CountByLabelDTO> applicantsByLevelStudy;
    private List<CountByLabelDTO> banksByCompany;
    private Double averageGlobalScore;
    private Boolean hasEnoughData;
    private String message;
}
