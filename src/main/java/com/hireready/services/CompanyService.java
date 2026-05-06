package com.hireready.services;

import com.hireready.dtos.CompanyResponseDTO;
import com.hireready.dtos.CompanyUpdateDTO;
import com.hireready.dtos.RegisterCompanyRequestDTO;
import com.hireready.entities.Company;

public interface CompanyService {
    public CompanyResponseDTO register(RegisterCompanyRequestDTO registerCompanyRequestDTO);
    public Company findByUserId(Long userId);
    public CompanyResponseDTO getProfile(Long userId);
    public CompanyResponseDTO updateProfile(Long userId, CompanyUpdateDTO companyUpdateDTO);
}