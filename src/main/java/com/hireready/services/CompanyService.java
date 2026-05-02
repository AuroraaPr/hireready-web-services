package com.hireready.services;

import com.hireready.dtos.CompanyDTO;
import com.hireready.entities.Company;

public interface CompanyService {
    public Company register(CompanyDTO dto);
}