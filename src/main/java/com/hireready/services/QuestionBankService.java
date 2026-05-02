package com.hireready.services;

import com.hireready.dtos.CreateQuestionBankRequest;
import com.hireready.dtos.QuestionBankListResponse;
import com.hireready.entities.*;

import java.util.List;

public interface QuestionBankService {

    public QuestionBank createQuestionBank(CreateQuestionBankRequest request);
    public List<QuestionBank> getAllQuestionBanks();
    public List<QuestionBankListResponse> listForApplicant();
    public QuestionBank findById(Long id);
}