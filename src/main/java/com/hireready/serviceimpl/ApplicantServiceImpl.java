package com.hireready.serviceimpl;

import com.hireready.dtos.ApplicantDTO;
import com.hireready.dtos.ApplicantUpdateDTO;
import com.hireready.entities.Applicant;
import com.hireready.entities.User;
import com.hireready.repositories.ApplicantRepository;
import com.hireready.services.ApplicantService;
import com.hireready.services.CareerService;
import com.hireready.services.UserService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ApplicantServiceImpl implements ApplicantService {
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CareerService careerService;

    @Override
    public Applicant findById(Long id){
        return applicantRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Applicant register(ApplicantDTO dto) {

        if (userService.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole("ROLE_APPLICANT");
        User savedUser = userService.save(user);

        Applicant applicant = new Applicant();
        applicant.setName(dto.getName());
        applicant.setLevel_study(dto.getLevel_study());
        applicant.setUniversity(dto.getUniversity());
        applicant.setUser(savedUser);

        return applicantRepository.save(applicant);
    }

//    @Transactional
//    @Override
//    public Applicant updateMe(ApplicantUpdateDTO dto, Authentication auth) {
//
//        String email = auth.getName();
//        User user = userService.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//
//
//        Applicant applicant = applicantRepository.findByUserId(user.getId())
//                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
//
//
//        if (dto.getCareerId() != null) {
//            if (!careerRepository.existsById(dto.getCareerId())) {
//                throw new RuntimeException("La carrera seleccionada no existe");
//            }
//        }
//
//        applicant.setName(dto.getName());
//        applicant.setBornDate(dto.getBornDate());
//        applicant.setLevel_study(dto.getLevelStudy());
//        applicant.setUniversity(dto.getUniversity());
//
//        return applicantRepository.save(applicant);
//    }

}
