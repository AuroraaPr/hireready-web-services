package com.hireready.serviceimpl;

import com.hireready.dtos.LoginRequestDTO;
import com.hireready.dtos.LoginResponseDTO;
import com.hireready.dtos.UserStatusResponseDTO;
import com.hireready.entities.User;
import com.hireready.enums.AuthorityRole;
import com.hireready.exceptions.DuplicateResourceException;
import com.hireready.exceptions.ForbiddenException;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.UserRepository;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    // US04 y US05
    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException("User id: " + id + " not found");
        }
        return user;
    }

    // US01 y US02
    @Override
    public User add(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("User email can not be blank");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ValidationException("User password can not be blank");
        }
        if (user.getAuthority() == null) {
            throw new ValidationException("User authority can not be null");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new DuplicateResourceException("Email " + user.getEmail() + " is already registered");
        }
        if (user.getEnabled() == null) user.setEnabled(true);
        return userRepository.save(user);
    }

    // US01, US02, US03
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // US03
    @Override
    public User login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ValidationException("Email and password are required");
        }
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new ValidationException("Invalid credentials");
        }
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new ForbiddenException("User account is disabled");
        }
        return user;
    }

    // US04
    @Override
    public void validateRole(Long userId, AuthorityRole expectedRole) {
        User user = findById(userId);
        if (user.getAuthority() == null || user.getAuthority().getRole() != expectedRole) {
            throw new ForbiddenException(
                    "User id: " + userId + " does not have role " + expectedRole);
        }
    }

    // US03
    @Override
    public LoginResponseDTO loginAsDto(LoginRequestDTO loginRequestDTO) {
        User user = login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        Long applicantId = user.getApplicant() != null ? user.getApplicant().getId() : null;
        Long companyId   = user.getCompany()   != null ? user.getCompany().getId()   : null;
        return new LoginResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getAuthority().getRole(),
                applicantId,
                companyId
        );
    }

    // US19
    @Override
    public UserStatusResponseDTO setEnabled(Long adminUserId, Long targetUserId, boolean enabled) {
        validateRole(adminUserId, AuthorityRole.ADMIN);
        if (adminUserId.equals(targetUserId)) {
            throw new ValidationException("Admin cannot change their own active status");
        }
        User target = findById(targetUserId);
        if (target.getAuthority() != null && target.getAuthority().getRole() == AuthorityRole.ADMIN) {
            throw new ValidationException("Cannot deactivate or activate another admin account");
        }
        target.setEnabled(enabled);
        target = userRepository.save(target);
        return new UserStatusResponseDTO(
                target.getId(), target.getEmail(),
                target.getAuthority() != null ? target.getAuthority().getRole() : null,
                target.getEnabled()
        );
    }
}
