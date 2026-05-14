package com.hireready.services;

import com.hireready.dtos.ApplicantDashboardResponseDTO;
import com.hireready.dtos.CompanyDashboardResponseDTO;
import com.hireready.dtos.DashboardResponseDTO;

public interface DashboardService {
    DashboardResponseDTO getMetrics(Long adminUserId);
    ApplicantDashboardResponseDTO getApplicantMetrics(Long applicantUserId);
    CompanyDashboardResponseDTO getCompanyMetrics(Long companyUserId);
}
