package com.hireready.services;

import com.hireready.dtos.CreateQuestionBankRequestDTO;
import com.hireready.dtos.QuestionBankDetailResponseDTO;
import com.hireready.dtos.QuestionBankResponseDTO;
import com.hireready.dtos.QuestionBankSummaryResponseDTO;
import com.hireready.entities.*;

import java.util.List;

public interface QuestionBankService {

    public QuestionBankResponseDTO create(Long companyUserId, CreateQuestionBankRequestDTO createQuestionBankRequestDTO);
    public QuestionBank findById(Long id);
    public List<QuestionBankSummaryResponseDTO> listAvailableForApplicant(Long applicantUserId, String filter);
    public QuestionBankDetailResponseDTO getQuestionBankById(Long id);

}