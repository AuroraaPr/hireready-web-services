package com.hireready.repositories;

import com.hireready.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByUserId(Long userId);
    List<Company> findAll();
}