package com.hireready.serviceimpl;

import com.hireready.dtos.ApplicantResponseDTO;
import com.hireready.dtos.ApplicantSummaryResponseDTO;
import com.hireready.dtos.ApplicantUpdateDTO;
import com.hireready.dtos.RegisterApplicantRequestDTO;
import com.hireready.entities.Applicant;
import com.hireready.entities.Authority;
import com.hireready.entities.Career;
import com.hireready.entities.User;
import com.hireready.enums.AuthorityRole;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.ApplicantRepository;
import com.hireready.services.ApplicantService;
import com.hireready.services.AuthorityService;
import com.hireready.services.CareerService;
import com.hireready.services.UserService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ApplicantServiceImpl implements ApplicantService {
    @Autowired
    ApplicantRepository applicantRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    CareerService careerService;

    String[] studyLevels = {
            "Secundaria completa",
            "Certificación técnica",
            "Curso especializado",
            "Bootcamp",
            "Pregrado (ciclos 1 al 3)",
            "Pregrado (ciclos 4 al 6)",
            "Pregrado (ciclos 7 al 10)",
            "Egresado",
            "Diplomado",
            "Maestría",
            "Doctorado"
    };

    // US05, US07, US08
    @Override
    public Applicant findByUserId(Long userId) {
        User user = userService.findById(userId);
        Applicant applicant = user.getApplicant();
        if (applicant == null) {
            throw new ResourceNotFoundException("User id: " + userId + " is not an applicant");
        }
        return applicant;
    }

    @Transactional
    @Override
    public ApplicantResponseDTO register(RegisterApplicantRequestDTO registerApplicantRequestDTO) {
        if (registerApplicantRequestDTO.getEmail() == null || registerApplicantRequestDTO.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (registerApplicantRequestDTO.getPassword() == null || registerApplicantRequestDTO.getPassword().isBlank()) {
            throw new ValidationException("Password is required");
        }
        if (registerApplicantRequestDTO.getName() == null || registerApplicantRequestDTO.getName().isBlank()) {
            throw new ValidationException("Name is required");
        }
        if (registerApplicantRequestDTO.getBornDate() == null || !registerApplicantRequestDTO.getBornDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Born date is required and must be in the past");
        }
        if (registerApplicantRequestDTO.getCareerId() == null) {
            throw new ValidationException("Career is required");
        }
        if (registerApplicantRequestDTO.getLevelStudy() == null || registerApplicantRequestDTO.getLevelStudy().isBlank()) {
            throw new ValidationException("Level of study is required");
        }
        if (!Arrays.asList(studyLevels).contains(registerApplicantRequestDTO.getLevelStudy())) {
            throw new ValidationException("Level of study is not valid");
        }

        Career career = careerService.findById(registerApplicantRequestDTO.getCareerId());
        Authority authority = authorityService.findByRole(AuthorityRole.APPLICANT);

        User user = new User(
                null,
                registerApplicantRequestDTO.getEmail(),
                registerApplicantRequestDTO.getPassword(),
                true,
                authority,
                null,
                null
        );

        user = userService.add(user);

        Applicant applicant = new Applicant(
                null,
                registerApplicantRequestDTO.getName(),
                registerApplicantRequestDTO.getBornDate(),
                registerApplicantRequestDTO.getLevelStudy(),
                registerApplicantRequestDTO.getUniversity(),
                user,
                career,
                null
        );
        applicant = applicantRepository.save(applicant);

        return toResponse(applicant);
    }

    // US05
    @Override
    public ApplicantResponseDTO getProfile(Long userId) {
        userService.validateRole(userId, AuthorityRole.APPLICANT);
        userService.validateOwnership(userId);
        return toResponse(findByUserId(userId));
    }

    // US05
    @Transactional
    @Override
    public ApplicantResponseDTO updateProfile(Long userId, ApplicantUpdateDTO applicantUpdateDTO) {
        userService.validateRole(userId, AuthorityRole.APPLICANT);
        userService.validateOwnership(userId);

        Applicant applicant = findByUserId(userId);

        if (applicantUpdateDTO.getName() != null && !applicantUpdateDTO.getName().isBlank()) {
            applicant.setName(applicantUpdateDTO.getName());
        }
        if (applicantUpdateDTO.getBornDate() != null) {
            if (!applicantUpdateDTO.getBornDate().isBefore(LocalDate.now())) {
                throw new ValidationException("Born date must be in the past");
            }
            applicant.setBornDate(applicantUpdateDTO.getBornDate());
        }
        if (applicantUpdateDTO.getLevelStudy() != null && !applicantUpdateDTO.getLevelStudy().isBlank()) {
            if (!Arrays.asList(studyLevels).contains(applicantUpdateDTO.getLevelStudy())) {
                throw new ValidationException("Level of study is not valid");
            }
            applicant.setLevelStudy(applicantUpdateDTO.getLevelStudy());
        }
        if (applicantUpdateDTO.getUniversity() != null) {
            applicant.setUniversity(applicantUpdateDTO.getUniversity());
        }
        if (applicantUpdateDTO.getCareerId() != null) {
            applicant.setCareer(careerService.findById(applicantUpdateDTO.getCareerId()));
        }

        applicant = applicantRepository.save(applicant);
        return toResponse(applicant);
    }

    @Override
    public List<ApplicantSummaryResponseDTO> listAll(Long adminUserId) {
        userService.validateRole(adminUserId, AuthorityRole.ADMIN); // US-04
        userService.validateOwnership(adminUserId);
        List<ApplicantSummaryResponseDTO> result = new ArrayList<>();
        for (Applicant a : applicantRepository.findAll()) {
            result.add(new ApplicantSummaryResponseDTO(
                    a.getId(),
                    a.getUser() != null ? a.getUser().getId() : null,
                    a.getUser() != null ? a.getUser().getEmail() : null,
                    a.getName(),
                    a.getBornDate(),
                    a.getCareer() != null ? a.getCareer().getName() : null,
                    a.getLevelStudy(),
                    a.getUniversity(),
                    a.getUser() != null ? a.getUser().getEnabled() : null,
                    a.getSimulations() != null ? a.getSimulations().size() : 0
            ));
        }
        return result;
    }

    private ApplicantResponseDTO toResponse(Applicant a) {
        return new ApplicantResponseDTO(
                a.getId(),
                a.getUser().getId(),
                a.getUser().getEmail(),
                a.getName(),
                a.getBornDate(),
                a.getCareer().getId(),
                a.getCareer().getName(),
                a.getLevelStudy(),
                a.getUniversity()
        );
    }
}
