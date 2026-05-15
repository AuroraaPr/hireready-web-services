package com.hireready.repositories;

import com.hireready.dtos.CountByLabelDTO;
import com.hireready.entities.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {
    // US08
    @Query("SELECT DISTINCT qb FROM QuestionBank qb " +
            "JOIN qb.questionBankCareers qbc " +
            "WHERE qbc.career.id = ?1")
    public List<QuestionBank> findByCareerId(Long careerId);
    @Query("SELECT new com.hireready.dtos.CountByLabelDTO(c.name, COUNT(qb)) " +
            "FROM QuestionBank qb JOIN qb.company c " +
            "GROUP BY c.name ORDER BY COUNT(qb) DESC")
    public List<CountByLabelDTO> countByCompany();
    public List<QuestionBank> findByCompany_Id(Long companyId);
}
