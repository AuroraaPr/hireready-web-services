package com.hireready.services;

import com.hireready.dtos.ApplicantDTO;
import com.hireready.entities.Applicant;

public interface ApplicantService {
    public Applicant register(ApplicantDTO dto);
    public Applicant findById(Long id);
}
