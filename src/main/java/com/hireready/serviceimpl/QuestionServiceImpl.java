package com.hireready.serviceimpl;

import com.hireready.entities.Question;
import com.hireready.repositories.QuestionRepository;
import com.hireready.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    List<Question> findByQuestionBankIdOrderByOrderIndexAsc(Long questionBankId){
        return questionRepository.findByQuestionBankIdOrderByOrderIndexAsc(questionBankId);
    }
}
