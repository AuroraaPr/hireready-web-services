package com.hireready.serviceimpl;

import com.hireready.dtos.ApplicantUpdateDTO;
import com.hireready.entities.Applicant;
import com.hireready.entities.User;
import com.hireready.repositories.ApplicantRepository;
import com.hireready.repositories.CareerRepository;
import com.hireready.repositories.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ApplicantServiceImpl {
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CareerRepository careerRepository;

    @Transactional
    public Applicant updateMe(ApplicantUpdateDTO dto, Authentication auth) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        Applicant applicant = applicantRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));


        if (dto.getCareerId() != null) {
            if (!careerRepository.existsById(dto.getCareerId())) {
                throw new RuntimeException("La carrera seleccionada no existe");
            }
        }

        applicant.setName(dto.getName());
        applicant.setBornDate(dto.getBornDate());
        applicant.setLevel_study(dto.getLevelStudy());
        applicant.setUniversity(dto.getUniversity());

        return applicantRepository.save(applicant);
    }

}
