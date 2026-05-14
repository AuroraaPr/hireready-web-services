package com.hireready.repositories;

import com.hireready.dtos.CountByLabelDTO;
import com.hireready.entities.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    public Applicant findByUserId(Long userId);
    @Query("SELECT new com.hireready.dtos.CountByLabelDTO(c.name, COUNT(a)) " +
            "FROM Applicant a JOIN a.career c " +
            "GROUP BY c.name ORDER BY COUNT(a) DESC")
    public List<CountByLabelDTO> countByCareer();

    @Query("SELECT new com.hireready.dtos.CountByLabelDTO(a.levelStudy, COUNT(a)) " +
            "FROM Applicant a " +
            "WHERE a.levelStudy IS NOT NULL " +
            "GROUP BY a.levelStudy ORDER BY COUNT(a) DESC")
    public List<CountByLabelDTO> countByLevelStudy();
}