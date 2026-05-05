package com.hireready.serviceimpl;

import com.hireready.dtos.CompanyResponseDTO;
import com.hireready.dtos.CompanyUpdateDTO;
import com.hireready.dtos.RegisterCompanyRequestDTO;
import com.hireready.entities.Authority;
import com.hireready.entities.Company;
import com.hireready.entities.User;
import com.hireready.enums.AuthorityRole;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.CompanyRepository;
import com.hireready.repositories.UserRepository;
import com.hireready.services.AuthorityService;
import com.hireready.services.CompanyService;
import com.hireready.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    // US-02
    @Transactional
    @Override
    public CompanyResponseDTO register(RegisterCompanyRequestDTO registerCompanyRequestDTO) {
        if (registerCompanyRequestDTO.getEmail() == null || registerCompanyRequestDTO.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (registerCompanyRequestDTO.getPassword() == null || registerCompanyRequestDTO.getPassword().isBlank()) {
            throw new ValidationException("Password is required");
        }
        if (registerCompanyRequestDTO.getName() == null || registerCompanyRequestDTO.getName().isBlank()) {
            throw new ValidationException("Company name is required");
        }
        if (registerCompanyRequestDTO.getDescription() == null || registerCompanyRequestDTO.getDescription().isBlank()) {
            throw new ValidationException("Company description is required");
        }

        Authority authority = authorityService.findByRole(AuthorityRole.COMPANY);

        User user = new User(
                null,
                registerCompanyRequestDTO.getEmail(),
                registerCompanyRequestDTO.getPassword(),
                true,
                authority,
                null,
                null
        );
        user = userService.add(user);

        Company company = new Company(
                null,
                registerCompanyRequestDTO.getName(),
                registerCompanyRequestDTO.getDescription(),
                user,
                null
        );
        company = companyRepository.save(company);

        return toResponse(company);
    }

    // US-05, US-06
    @Override
    public Company findByUserId(Long userId) {
        User user = userService.findById(userId);
        Company company = user.getCompany();
        if (company == null) {
            throw new ResourceNotFoundException("User id: " + userId + " is not a company");
        }
        return company;
    }

    // US-05
    @Override
    public CompanyResponseDTO getProfile(Long userId) {
        userService.validateRole(userId, AuthorityRole.COMPANY);
        return toResponse(findByUserId(userId));
    }

    // US-05
    @Override
    public CompanyResponseDTO updateProfile(Long userId, CompanyUpdateDTO companyUpdateDTO) {
        userService.validateRole(userId, AuthorityRole.COMPANY);
        Company company = findByUserId(userId);

        if (companyUpdateDTO.getName() != null && !companyUpdateDTO.getName().isBlank()) {
            company.setName(companyUpdateDTO.getName());
        }
        if (companyUpdateDTO.getDescription() != null && !companyUpdateDTO.getDescription().isBlank()) {
            company.setDescription(companyUpdateDTO.getDescription());
        }
        company = companyRepository.save(company);
        return toResponse(company);
    }

    private CompanyResponseDTO toResponse(Company c) {
        return new CompanyResponseDTO(
                c.getId(),
                c.getUser().getId(),
                c.getUser().getEmail(),
                c.getName(),
                c.getDescription()
        );
    }
}