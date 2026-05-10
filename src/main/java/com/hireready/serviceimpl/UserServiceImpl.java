package com.hireready.serviceimpl;

import com.hireready.dtos.LoginRequestDTO;
import com.hireready.dtos.LoginResponseDTO;
import com.hireready.dtos.UserDTO;
import com.hireready.entities.Authority;
import com.hireready.entities.User;
import com.hireready.enums.AuthorityRole;
import com.hireready.exceptions.DuplicateResourceException;
import com.hireready.exceptions.ForbiddenException;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.UserRepository;
import com.hireready.services.AuthorityService;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityService authorityService;

    // US04 y US05
    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException("User id: " + id + " not found");
        }
        return user;
    }

    private List<Authority> authoritiesFromString(String authorities) {

        List<Authority> authorityList = new ArrayList<>();
        List<String> authorityStringList = Arrays.stream(authorities.split(";")).toList();
        for (String authorityString : authorityStringList) {
            Authority authority = authorityService.findByRole(authorityString);
            if (authority != null) {
                authorityList.add(authority);
            }
        }
        return authorityList;
    }

    // US01 y US02
    @Override
    public UserDTO add(UserDTO userDTO) {
        List<Authority> authorityList = authoritiesFromString(userDTO.getAuthorities());

        User newUser = new User(null, userDTO.getEmail(),
                new BCryptPasswordEncoder().encode(userDTO.getPassword()), false, authorityList);

        newUser = userRepository.save(newUser);
        userDTO.setId(newUser.getId());
        return userDTO;
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
}
