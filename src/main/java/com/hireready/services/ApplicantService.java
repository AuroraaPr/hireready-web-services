package com.hireready.services;

import com.hireready.dtos.ApplicantResponseDTO;
import com.hireready.dtos.ApplicantSummaryResponseDTO;
import com.hireready.dtos.ApplicantUpdateDTO;
import com.hireready.dtos.RegisterApplicantRequestDTO;
import com.hireready.entities.Applicant;

import java.util.List;

public interface ApplicantService {
    public ApplicantResponseDTO register(RegisterApplicantRequestDTO applicantRequestDTO);
    public Applicant findByUserId(Long userId);
    public ApplicantResponseDTO getProfile(Long userId);
    public ApplicantResponseDTO updateProfile(Long userId, ApplicantUpdateDTO applicantUpdateDTO);
    public List<ApplicantSummaryResponseDTO> listAll();
}
