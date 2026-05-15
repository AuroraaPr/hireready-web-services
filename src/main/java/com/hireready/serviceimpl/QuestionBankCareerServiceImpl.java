package com.hireready.serviceimpl;

import com.hireready.entities.Career;
import com.hireready.entities.QuestionBank;
import com.hireready.entities.QuestionBankCareer;
import com.hireready.repositories.QuestionBankCareerRepository;
import com.hireready.services.QuestionBankCareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionBankCareerServiceImpl implements QuestionBankCareerService {
    @Autowired
    QuestionBankCareerRepository questionBankCareerRepository;

    // US06
    @Override
    public List<QuestionBankCareer> link(QuestionBank bank, List<Career> careers) {
        List<QuestionBankCareer> created = new ArrayList<>();
        for (Career c : careers) {
            QuestionBankCareer qbc = new QuestionBankCareer(
                    null,
                    c,
                    bank
            );
            created.add(questionBankCareerRepository.save(qbc));
        }
        return created;
    }
}
