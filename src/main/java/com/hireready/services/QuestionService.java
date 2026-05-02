package com.hireready.services;

import com.hireready.entities.Question;

import java.util.List;

public interface QuestionService {
    List<Question> findByQuestionBankIdOrderByOrderIndexAsc(Long questionBankId);
}
