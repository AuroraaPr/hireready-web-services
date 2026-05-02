package com.hireready.services;

import com.hireready.dtos.CreateQuestionBankRequest;
import com.hireready.dtos.QuestionBankListResponse;
import com.hireready.entities.*;
import com.hireready.repositories.CareerRepository;
import com.hireready.repositories.QuestionBankRepository;
import com.hireready.repositories.SimulationRepository;
import java.util.ArrayList;
import java.util.List;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

public interface QuestionBankService {

    public QuestionBank createQuestionBank(CreateQuestionBankRequest request);
    public List<QuestionBank> getAllQuestionBanks();
    public List<QuestionBankListResponse> listForApplicant();
}