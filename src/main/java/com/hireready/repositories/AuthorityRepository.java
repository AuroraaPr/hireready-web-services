package com.hireready.repositories;

import com.hireready.entities.Authority;
import com.hireready.enums.AuthorityRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByRole(AuthorityRole authorityRole);
}
