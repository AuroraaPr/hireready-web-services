package com.hireready.serviceimpl;

import com.hireready.entities.Authority;
import com.hireready.enums.AuthorityRole;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.repositories.AuthorityRepository;
import com.hireready.services.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityRepository authorityRepository;

    // US01, US02
    @Override
    public Authority findByRole(AuthorityRole role) {
        Authority authority = authorityRepository.findByRole(role);
        if (authority == null) {
            throw new ResourceNotFoundException("Authority for role " + role + " not configured");
        }
        return authority;
    }
}
