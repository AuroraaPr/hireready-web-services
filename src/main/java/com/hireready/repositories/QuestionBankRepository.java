package com.hireready.repositories;

import com.hireready.entities.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {
    // US08
    @Query("SELECT DISTINCT qb FROM QuestionBank qb " +
            "JOIN qb.questionBankCareers qbc " +
            "WHERE qbc.career.id = ?1")
    List<QuestionBank> findByCareerId(Long careerId);
}
