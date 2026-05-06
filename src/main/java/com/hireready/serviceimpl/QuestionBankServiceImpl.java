package com.hireready.serviceimpl;

import com.hireready.dtos.CreateQuestionBankRequestDTO;
import com.hireready.dtos.QuestionBankResponseDTO;
import com.hireready.dtos.QuestionBankSummaryResponseDTO;
import com.hireready.dtos.QuestionResponseDTO;
import com.hireready.entities.*;
import com.hireready.enums.AuthorityRole;
import com.hireready.enums.SimulationStatus;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.repositories.QuestionBankRepository;
import com.hireready.services.*;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {
    @Autowired
    QuestionBankRepository questionBankRepository;

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @Autowired
    CareerService careerService;

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionBankCareerService questionBankCareerService;

    @Autowired
    ApplicantService applicantService;

    // US06
    String[] validBankLevels = {
            "Sin experiencia",
            "Practicante",
            "Junior",
            "Semi Senior",
            "Senior"
    };

    // US06
    @Override
    public QuestionBankResponseDTO create(Long companyUserId, CreateQuestionBankRequestDTO request) {
        userService.validateRole(companyUserId, AuthorityRole.COMPANY); // US04
        Company company = companyService.findByUserId(companyUserId);

        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("Bank name is required");
        }
        if (request.getJobPosition() == null || request.getJobPosition().isBlank()) {
            throw new ValidationException("Job position is required");
        }
        if (request.getLevel() == null || !Arrays.asList(validBankLevels).contains(request.getLevel())) {
            throw new ValidationException("Bank level is required and must be one of " + Arrays.toString(validBankLevels));
        }
        if (request.getCareerIds() == null || request.getCareerIds().isEmpty()) {
            throw new ValidationException("At least one career must be selected");
        }
        if (request.getQuestions() == null || request.getQuestions().isEmpty()) {
            throw new ValidationException("At least one question is required");
        }

        List<Career> careers = new ArrayList<>();
        for (Long cid : new HashSet<>(request.getCareerIds())) {
            careers.add(careerService.findById(cid));
        }

        QuestionBank bank = new QuestionBank(
                null,
                request.getName(),
                request.getDescription(),
                request.getJobPosition(),
                request.getLevel(),
                company,
                null,
                null,
                null
        );
        bank = questionBankRepository.save(bank);

        List<QuestionBankCareer> qbcs = questionBankCareerService.link(bank, careers);
        List<Question> questions = questionService.bulkCreate(bank, request.getQuestions());

        bank.setQuestionBankCareers(qbcs);
        bank.setQuestions(questions);

        return toFullResponse(bank);
    }

    // US09
    @Override
    public QuestionBank findById(Long id) {
        QuestionBank bank = questionBankRepository.findById(id).orElse(null);
        if (bank == null) {
            throw new ResourceNotFoundException("QuestionBank id: " + id + " not found");
        }
        return bank;
    }

    // US07 y US08
    @Override
    public List<QuestionBankSummaryResponseDTO> listAvailableForApplicant(Long applicantUserId, String filter) {
        userService.validateRole(applicantUserId, AuthorityRole.APPLICANT); // US04
        Applicant applicant = applicantService.findByUserId(applicantUserId);

        List<QuestionBank> banks;
        if ("recommended".equalsIgnoreCase(filter)) {
            // US08
            if (applicant.getCareer() == null) {
                return new ArrayList<>();
            }
            banks = questionBankRepository.findByCareerId(applicant.getCareer().getId());
        } else {
            // US07/US08
            banks = questionBankRepository.findAll();
        }

        List<QuestionBankSummaryResponseDTO> result = new ArrayList<>();
        for (QuestionBank bank : banks) {
            result.add(toSummary(bank, applicant));
        }
        return result;
    }

    private QuestionBankResponseDTO toFullResponse(QuestionBank b) {
        List<Long> careerIds = new ArrayList<>();
        List<String> careerNames = new ArrayList<>();
        if (b.getQuestionBankCareers() != null) {
            for (QuestionBankCareer qbc : b.getQuestionBankCareers()) {
                careerIds.add(qbc.getCareer().getId());
                careerNames.add(qbc.getCareer().getName());
            }
        }
        List<QuestionResponseDTO> qs = new ArrayList<>();
        if (b.getQuestions() != null) {
            for (Question q : b.getQuestions()) {
                qs.add(new QuestionResponseDTO(q.getId(), q.getContent(), q.getOrderIndex()));
            }
        }
        return new QuestionBankResponseDTO(
                b.getId(), b.getName(), b.getDescription(), b.getJobPosition(), b.getLevel(),
                b.getCompany().getId(), b.getCompany().getName(),
                careerIds, careerNames, qs
        );
    }

    // US07
    private QuestionBankSummaryResponseDTO toSummary(QuestionBank b, Applicant applicant) {
        List<String> careerNames = new ArrayList<>();
        if (b.getQuestionBankCareers() != null) {
            careerNames = b.getQuestionBankCareers().stream()
                    .map(qbc -> qbc.getCareer().getName())
                    .collect(Collectors.toList());
        }
        int numQuestions = b.getQuestions() == null ? 0 : b.getQuestions().size();

        String status = "NOT_STARTED";
        if (b.getSimulations() != null) {
            boolean hasInProgress = false;
            boolean hasCompleted = false;
            for (Simulation s : b.getSimulations()) {
                if (s.getApplicant() != null && s.getApplicant().getId().equals(applicant.getId())) {
                    if (s.getStatus() == SimulationStatus.IN_PROGRESS) hasInProgress = true;
                    if (s.getStatus() == SimulationStatus.COMPLETED) hasCompleted = true;
                }
            }
            if (hasInProgress) status = "IN_PROGRESS";
            else if (hasCompleted) status = "COMPLETED";
        }

        return new QuestionBankSummaryResponseDTO(
                b.getId(), b.getName(),
                b.getCompany() != null ? b.getCompany().getName() : null,
                b.getDescription(), b.getJobPosition(), b.getLevel(),
                careerNames, numQuestions, status
        );
    }
}
