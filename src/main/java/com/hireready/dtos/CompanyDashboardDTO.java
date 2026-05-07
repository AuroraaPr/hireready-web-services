package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDashboardDTO {
    private Long totalSimulationsPerCompany;
    private Double generalAverageScore;
    private List<CareerMetricDTO> careerDistribution;
}
