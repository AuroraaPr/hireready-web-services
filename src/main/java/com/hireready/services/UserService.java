package com.hireready.services;

import com.hireready.dtos.LoginRequestDTO;
import com.hireready.dtos.LoginResponseDTO;
import com.hireready.entities.User;
import com.hireready.enums.AuthorityRole;

public interface UserService {
    User add(User user);
    User findById(Long id);
    User findByEmail(String email);
    public Boolean existsByEmail(String email);
    User login(String email, String password);
    void validateRole(Long userId, AuthorityRole expectedRole);
    LoginResponseDTO loginAsDto(LoginRequestDTO loginRequestDTO);
}