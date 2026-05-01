package com.hireready.serviceimpl;

import com.hireready.dtos.CompanyDTO;
import com.hireready.entities.Company;
import com.hireready.entities.User;
import com.hireready.repositories.CompanyRepository;
import com.hireready.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional // Asegura que se guarden ambos o ninguno
    public Company registerCompany(CompanyDTO dto) {
        // 1. Validación de email único
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        // 2. Crear y guardar el Usuario base con el rol
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole("ROLE_COMPANY"); // Asignación del rol según la US-02
        User savedUser = userRepository.save(user);

        // 3. Crear y guardar la Compañía vinculada
        Company company = new Company();
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setUser(savedUser); // Relación User-Company del ERD

        return companyRepository.save(company);
    }
}