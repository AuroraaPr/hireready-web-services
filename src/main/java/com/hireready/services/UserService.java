package com.hireready.services;

import com.hireready.entities.User;

public interface UserService {
    public User register(User user);
    public Boolean existsByEmail(String email);
}