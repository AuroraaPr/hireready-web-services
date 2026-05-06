package com.hireready.services;

import com.hireready.dtos.LoginRequestDTO;
import com.hireready.dtos.LoginResponseDTO;
import com.hireready.entities.User;
import com.hireready.enums.AuthorityRole;

public interface UserService {
    public User add(User user);
    public User findById(Long id);
    public User findByEmail(String email);
    public Boolean existsByEmail(String email);
    public User login(String email, String password);
    public void validateRole(Long userId, AuthorityRole expectedRole);
    public LoginResponseDTO loginAsDto(LoginRequestDTO loginRequestDTO);
}