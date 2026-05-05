package com.hireready.services;

import com.hireready.dtos.CompanyResponseDTO;
import com.hireready.dtos.CompanyUpdateDTO;
import com.hireready.dtos.RegisterCompanyRequestDTO;
import com.hireready.entities.Company;

public interface CompanyService {
    public CompanyResponseDTO register(RegisterCompanyRequestDTO registerCompanyRequestDTO);
    Company findByUserId(Long userId);
    CompanyResponseDTO getProfile(Long userId);
    CompanyResponseDTO updateProfile(Long userId, CompanyUpdateDTO companyUpdateDTO);
}