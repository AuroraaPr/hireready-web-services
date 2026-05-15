package com.hireready.services;

import com.hireready.dtos.UserStatusResponseDTO;
import com.hireready.entities.User;
import com.hireready.enums.AuthorityRole;

public interface UserService {
    public User add(User user);
    public User findById(Long id);
    public User findByEmail(String email);
    public Boolean existsByEmail(String email);
    public void validateRole(Long userId, AuthorityRole expectedRole);
    public UserStatusResponseDTO setEnabled(Long adminUserId, Long targetUserId, boolean enabled);
    public Long getAuthenticatedUserId();
    public void validateOwnership(Long pathUserId);
}