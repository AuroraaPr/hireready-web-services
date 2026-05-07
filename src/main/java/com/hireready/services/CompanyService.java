package com.hireready.services;

import com.hireready.dtos.CompanyDashboardDTO;
import com.hireready.dtos.CompanyResponseDTO;
import com.hireready.dtos.CompanyUpdateDTO;
import com.hireready.dtos.RegisterCompanyRequestDTO;
import com.hireready.entities.Company;

import java.util.List;

public interface CompanyService {
    public CompanyResponseDTO register(RegisterCompanyRequestDTO registerCompanyRequestDTO);
    public Company findByUserId(Long userId);
    public CompanyResponseDTO getProfile(Long userId);
    public CompanyResponseDTO updateProfile(Long userId, CompanyUpdateDTO companyUpdateDTO);

    List<Company> getAllCompanies();

    CompanyDashboardDTO getCompanyDashboard(Long companyId);
}