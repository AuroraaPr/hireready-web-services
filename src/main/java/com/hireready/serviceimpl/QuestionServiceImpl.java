package com.hireready.serviceimpl;

import com.hireready.dtos.QuestionRequestDTO;
import com.hireready.entities.Question;
import com.hireready.entities.QuestionBank;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.QuestionRepository;
import com.hireready.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    // US06
    @Override
    public List<Question> bulkCreate(QuestionBank bank, List<QuestionRequestDTO> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new ValidationException("At least one question is required");
        }
        Set<Integer> seenOrders = new HashSet<>();
        List<Question> created = new ArrayList<>();
        for (QuestionRequestDTO qr : requests) {
            if (qr.getContent() == null || qr.getContent().isBlank()) {
                throw new ValidationException("Question content can not be blank");
            }
            if (qr.getOrderIndex() == null || qr.getOrderIndex() < 1) {
                throw new ValidationException("Question orderIndex must be >= 1");
            }
            if (!seenOrders.add(qr.getOrderIndex())) {
                throw new ValidationException("Duplicated question orderIndex: " + qr.getOrderIndex());
            }
            Question q = new Question(null, qr.getContent(), qr.getOrderIndex(), bank, null);
            created.add(questionRepository.save(q));
        }
        return created;
    }

    // US10
    @Override
    public List<Question> listByBankOrdered(Long questionBankId) {
        return questionRepository.findByQuestionBank_IdOrderByOrderIndexAsc(questionBankId);
    }
}
