package com.hireready.services;

import com.hireready.dtos.ApplicantResponseDTO;
import com.hireready.dtos.ApplicantUpdateDTO;
import com.hireready.dtos.RegisterApplicantRequestDTO;
import com.hireready.entities.Applicant;

public interface ApplicantService {
    public ApplicantResponseDTO register(RegisterApplicantRequestDTO applicantRequestDTO);
    public Applicant findByUserId(Long userId);
    ApplicantResponseDTO getProfile(Long userId);
    ApplicantResponseDTO updateProfile(Long userId, ApplicantUpdateDTO applicantUpdateDTO);
}
