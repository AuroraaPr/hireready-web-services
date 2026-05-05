package com.hireready.repositories;

import com.hireready.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestionBank_IdOrderByOrderIndexAsc(Long questionBankId);
}
