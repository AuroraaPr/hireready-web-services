package com.hireready.services;

import com.hireready.entities.Authority;
import com.hireready.enums.AuthorityRole;

public interface AuthorityService {
    Authority findByRole(AuthorityRole authorityRole);
}
