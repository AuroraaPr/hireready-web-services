package com.hireready.services;

import com.hireready.dtos.QuestionRequestDTO;
import com.hireready.entities.Question;
import com.hireready.entities.QuestionBank;

import java.util.List;

public interface QuestionService {
    List<Question> bulkCreate(QuestionBank bank, List<QuestionRequestDTO> requests);
    List<Question> listByBankOrdered(Long questionBankId);
}
