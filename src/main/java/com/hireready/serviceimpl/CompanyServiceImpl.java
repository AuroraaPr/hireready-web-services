package com.hireready.serviceimpl;

import com.hireready.dtos.CompanyDTO;
import com.hireready.dtos.CompanyUpdateDTO;
import com.hireready.entities.Company;
import com.hireready.entities.User;
import com.hireready.repositories.CompanyRepository;
import com.hireready.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Company registerCompany(CompanyDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        // 2. Crear y guardar el Usuario base con el rol
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole("ROLE_COMPANY");
        User savedUser = userRepository.save(user);


        Company company = new Company();
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setUser(savedUser);

        return companyRepository.save(company);
    }

    @Transactional
    public Company updateMe(CompanyUpdateDTO dto, Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil de empresa no encontrado"));

        company.setName(dto.getName());
        company.setDescription(dto.getDescription());

        return companyRepository.save(company);
    }
}