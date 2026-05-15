package com.hireready.repositories;

import com.hireready.entities.Career;
import com.hireready.entities.QuestionBank;
import com.hireready.entities.QuestionBankCareer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBankCareerRepository extends JpaRepository<QuestionBankCareer, Long> {
}
