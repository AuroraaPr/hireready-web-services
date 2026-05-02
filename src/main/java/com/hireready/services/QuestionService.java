package com.hireready.services;

import com.hireready.entities.Question;

import java.util.List;

public interface QuestionService {
    public List<Question> findByQuestionBankIdOrderByOrderIndexAsc(Long questionBankId);
}
