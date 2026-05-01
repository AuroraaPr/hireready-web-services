package com.hireready.services;

import com.hireready.dtos.ApplicantDTO;
import com.hireready.entities.Applicant;
import com.hireready.entities.User;
import com.hireready.repositories.ApplicantRepository;
import com.hireready.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicantRepository applicantRepository;

    @Transactional
    public Applicant register(ApplicantDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole("ROLE_APPLICANT"); // Asignación del rol
        User savedUser = userRepository.save(user);

        Applicant applicant = new Applicant();
        applicant.setName(dto.getName());
        applicant.setLevel_study(dto.getLevel_study());
        applicant.setUniversity(dto.getUniversity());
        applicant.setUser(savedUser);

        return applicantRepository.save(applicant);
    }

}
