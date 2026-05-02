package com.hireready.services;

import com.hireready.dtos.CompanyDTO;
import com.hireready.entities.Company;
import com.hireready.entities.User;
import com.hireready.repositories.CompanyRepository;
import com.hireready.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public Company register(CompanyDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email corporativo ya está registrado");
        }


        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole("ROLE_COMPANY");
        User savedUser = userRepository.save(user);

        Company company = new Company();
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        // company.setActive(true); para mostrar si queremos que las empresas empiezan con una cuenta activa
        company.setUser(savedUser);
        return companyRepository.save(company);
    }
}