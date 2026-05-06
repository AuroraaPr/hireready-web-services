package com.hireready.services;

import com.hireready.entities.Career;
import com.hireready.entities.QuestionBank;
import com.hireready.entities.QuestionBankCareer;

import java.util.List;

public interface QuestionBankCareerService {
    public List<QuestionBankCareer> link(QuestionBank bank, List<Career> careers);
}
