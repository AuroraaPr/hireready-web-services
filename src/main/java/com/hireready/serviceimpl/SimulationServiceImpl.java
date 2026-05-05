package com.hireready.serviceimpl;

import com.hireready.dtos.*;
import com.hireready.entities.*;
import com.hireready.enums.AuthorityRole;
import com.hireready.enums.SimulationStatus;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.ResponseRepository;
import com.hireready.repositories.SimulationRepository;
import com.hireready.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SimulationServiceImpl implements SimulationService {
    @Autowired
    SimulationRepository simulationRepository;

    @Autowired
    ResponseRepository responseRepository;

    @Autowired
    UserService userService;

    @Autowired
    ApplicantService applicantService;

    @Autowired
    QuestionBankService questionBankService;

    @Autowired
    QuestionService questionService;

    // US09
    @Override
    public SimulationResponseDTO start(Long applicantUserId, SimulationStartRequestDTO request) {
        userService.validateRole(applicantUserId, AuthorityRole.APPLICANT); // US04

        if (request.getQuestionBankId() == null) {
            throw new ValidationException("questionBankId is required");
        }
        Applicant applicant = applicantService.findByUserId(applicantUserId);
        QuestionBank bank = questionBankService.findById(request.getQuestionBankId());

        //detectar simulaciones en progreso
        List<Simulation> inProgress = simulationRepository
                .findByApplicant_IdAndStatus(applicant.getId(), SimulationStatus.IN_PROGRESS);

        if (!inProgress.isEmpty()) {
            boolean confirm = Boolean.TRUE.equals(request.getConfirmAbandonPrevious());
            if (!confirm) {
                throw new ValidationException(
                        "There is a simulation in progress. Send confirmAbandonPrevious=true to abandon it and start a new one.");
            }
            // marca commo abandonada
            for (Simulation s : inProgress) {
                s.setStatus(SimulationStatus.ABANDONED);
                s.setCompletedAt(LocalDateTime.now());
                simulationRepository.save(s);
            }
        }

        // nueva simulación
        Simulation sim = new Simulation(
                null,
                LocalDateTime.now(),
                null,
                SimulationStatus.IN_PROGRESS,
                applicant,
                bank,
                null,
                null,
                null
        );
        sim = simulationRepository.save(sim);

        return toResponse(sim);
    }

    // US-10
    @Override
    public ContinueSimulationResponseDTO continueLatest(Long applicantUserId) {
        userService.validateRole(applicantUserId, AuthorityRole.APPLICANT); // US04
        Applicant applicant = applicantService.findByUserId(applicantUserId);

        Simulation sim = simulationRepository
                .findFirstByApplicant_IdAndStatusOrderByStartedAtDesc(
                        applicant.getId(), SimulationStatus.IN_PROGRESS);
        if (sim == null) {
            throw new ResourceNotFoundException("No in-progress simulation for applicant id: " + applicant.getId());
        }

        List<Question> ordered = questionService.listByBankOrdered(sim.getQuestionBank().getId());
        List<Response> answered = responseRepository.findBySimulation_Id(sim.getId());

        Set<Long> answeredQuestionIds = new HashSet<>();
        for (Response r : answered) {
            if (r.getQuestion() != null) answeredQuestionIds.add(r.getQuestion().getId());
        }

        Question pending = null;
        for (Question q : ordered) {
            if (!answeredQuestionIds.contains(q.getId())) {
                pending = q;
                break;
            }
        }
        if (pending == null) {
            // Todas respondidas: marcar la simulación como COMPLETED
            sim.setStatus(SimulationStatus.COMPLETED);
            sim.setCompletedAt(LocalDateTime.now());
            simulationRepository.save(sim);
            return new ContinueSimulationResponseDTO(
                    sim.getId(),
                    sim.getStatus(),
                    null,
                    ordered.size(),
                    answered.size());
        }

        QuestionResponseDTO pendingDto = new QuestionResponseDTO(
                pending.getId(),
                pending.getContent(),
                pending.getOrderIndex()
        );
        return new ContinueSimulationResponseDTO(
                sim.getId(),
                sim.getStatus(),
                pendingDto,
                ordered.size(),
                answered.size()
        );
    }

    private SimulationResponseDTO toResponse(Simulation s) {
        return new SimulationResponseDTO(
                s.getId(),
                s.getApplicant().getId(),
                s.getQuestionBank().getId(),
                s.getQuestionBank().getName(),
                s.getStatus(),
                s.getStartedAt(),
                s.getCompletedAt()
        );
    }
}
