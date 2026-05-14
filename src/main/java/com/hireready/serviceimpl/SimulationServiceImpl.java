package com.hireready.serviceimpl;

import com.hireready.dtos.*;
import com.hireready.entities.*;
import com.hireready.enums.AuthorityRole;
import com.hireready.enums.SimulationStatus;
import com.hireready.exceptions.ForbiddenException;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.SimulationRepository;
import com.hireready.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SimulationServiceImpl implements SimulationService {
    @Autowired
    SimulationRepository simulationRepository;

    @Autowired
    ResponseService responseService;

    @Autowired
    UserService userService;

    @Autowired
    ApplicantService applicantService;

    @Autowired
    QuestionBankService questionBankService;

    @Autowired
    QuestionService questionService;

    @Autowired
    ResponseAnalysisService responseAnalysisService;

    @Autowired
    FillerWordService fillerWordService;

    @Autowired
    SimulationReportService simulationReportService;

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

    // US10
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
        List<Response> answered = responseService.listBySimulationId(sim.getId());

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
            // si estan todas respondidas -> COMPLETED
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



    //US11
    @Override
    public SubmitResponseResponseDTO submitResponse(Long applicantUserId, Long simulationId, SubmitResponseRequestDTO request) {
        Simulation sim = findOwnedSimulation(applicantUserId, simulationId); // valida rol + ownership
        Response saved = responseService.submit(sim, request);

        // progreso y siguiente pregunta
        List<Question> ordered = questionService.listByBankOrdered(sim.getQuestionBank().getId());
        List<Response> answered = responseService.listBySimulationId(sim.getId());

        Set<Long> answeredIds = new HashSet<>();
        for (Response r : answered) {
            if (r.getQuestion() != null) answeredIds.add(r.getQuestion().getId());
        }

        Question next = null;
        for (Question q : ordered) {
            if (!answeredIds.contains(q.getId())) { next = q; break; }
        }
        boolean wasLast = (next == null);

        QuestionResponseDTO nextDto = next == null ? null :
                new QuestionResponseDTO(next.getId(), next.getContent(), next.getOrderIndex());

        return new SubmitResponseResponseDTO(
                sim.getId(),
                saved.getId(),
                nextDto,
                ordered.size(),
                answered.size(),
                wasLast
        );
    }

    // US12
    @Override
    public ExitSimulationResponseDTO exit(Long applicantUserId, Long simulationId) {
        Simulation sim = findOwnedSimulation(applicantUserId, simulationId);
        if (sim.getStatus() != SimulationStatus.IN_PROGRESS) {
            throw new ValidationException("Only in-progress simulations can be exited");
        }
        int total = questionService.listByBankOrdered(sim.getQuestionBank().getId()).size();
        int answered = responseService.listBySimulationId(sim.getId()).size();

        return new ExitSimulationResponseDTO(
                sim.getId(),
                sim.getStatus(),
                total,
                answered,
                "Progress saved. You can resume later."
        );
    }

    // US13
    @Override
    public FinalizeSimulationResponseDTO finalize(Long applicantUserId, Long simulationId) {
        Simulation sim = findOwnedSimulation(applicantUserId, simulationId);

        if (sim.getStatus() == SimulationStatus.COMPLETED) {
            throw new ValidationException("Simulation is already completed");
        }
        if (sim.getStatus() != SimulationStatus.IN_PROGRESS) {
            throw new ValidationException("Only in-progress simulations can be finalized");
        }

        List<Question> ordered = questionService.listByBankOrdered(sim.getQuestionBank().getId());
        List<Response> answered = responseService.listBySimulationId(sim.getId());
        if (answered.size() < ordered.size()) {
            throw new ValidationException(
                    "All questions must be answered before finalizing. Pending: " +
                            (ordered.size() - answered.size()));
        }

        for (Response r : answered) {
            if (r.getResponseAnalysis() == null) {
                ResponseAnalysis a = responseAnalysisService.analyzeAndSave(r);
                r.setResponseAnalysis(a);
            }
        }

        fillerWordService.detectAndSave(sim, answered);

        sim.setStatus(SimulationStatus.COMPLETED);
        sim.setCompletedAt(LocalDateTime.now());
        sim = simulationRepository.save(sim);

        // generar reporte
        SimulationReport report = simulationReportService.generate(sim, answered);

        return new FinalizeSimulationResponseDTO(
                sim.getId(), sim.getStatus(), sim.getCompletedAt(), report.getOverallScore());
    }

    //US14
    @Override
    public SimulationReportFullResponseDTO getReport(Long applicantUserId, Long simulationId) {
        Simulation sim = findOwnedSimulation(applicantUserId, simulationId);
        if (sim.getStatus() != SimulationStatus.COMPLETED) {
            throw new ValidationException("Only completed simulations have a report");
        }

        SimulationReport report = simulationReportService.findBySimulationId(sim.getId());
        List<FillerWord> fillers = fillerWordService.listBySimulationId(sim.getId());
        List<Response> responses = responseService.listBySimulationId(sim.getId());

        List<ResponseDetailResponseDTO> details = new ArrayList<>();
        for (Response r : responses) {
            ResponseAnalysis a = r.getResponseAnalysis();
            details.add(new ResponseDetailResponseDTO(
                    r.getId(),
                    r.getQuestion() != null ? r.getQuestion().getOrderIndex() : null,
                    r.getQuestion() != null ? r.getQuestion().getContent() : null,
                    r.getAudioUrl(),
                    r.getTranscription(),
                    r.getDuration(),
                    a != null ? a.getRelevanceScore() : null,
                    a != null ? a.getClarityScore()   : null,
                    a != null ? a.getStructureScore() : null,
                    a != null ? a.getFeedback() : null
            ));
        }
        details.sort(Comparator.comparing(
                ResponseDetailResponseDTO::getOrderIndex,
                Comparator.nullsLast(Integer::compareTo)));

        List<FillerWordResponseDTO> fillerDtos = new ArrayList<>();
        for (FillerWord fw : fillers) {
            fillerDtos.add(new FillerWordResponseDTO(fw.getWord(), fw.getCount()));
        }

        return new SimulationReportFullResponseDTO(
                sim.getId(),
                sim.getQuestionBank().getName(),
                sim.getQuestionBank().getCompany() != null
                        ? sim.getQuestionBank().getCompany().getName() : null,
                sim.getStartedAt(),
                sim.getCompletedAt(),
                report.getOverallScore(),
                report.getAvgRelevance(),
                report.getAvgClarity(),
                report.getAvgStructure(),
                report.getWordsPerMinute(),
                fillerDtos,
                details
        );
    }

    @Override
    public List<SimulationHistoryItemResponseDTO> listHistory(Long applicantUserId) {
        userService.validateRole(applicantUserId, AuthorityRole.APPLICANT);
        Applicant applicant = applicantService.findByUserId(applicantUserId);

        List<Simulation> sims = simulationRepository
                .findByApplicant_IdOrderByStartedAtDesc(applicant.getId());

        List<SimulationHistoryItemResponseDTO> result = new ArrayList<>();
        for (Simulation s : sims) {
            result.add(new SimulationHistoryItemResponseDTO(
                    s.getId(),
                    s.getQuestionBank() != null ? s.getQuestionBank().getName() : null,
                    s.getQuestionBank() != null && s.getQuestionBank().getCompany() != null
                            ? s.getQuestionBank().getCompany().getName() : null,
                    s.getStartedAt(),
                    s.getCompletedAt(),
                    s.getStatus(),
                    s.getStatus() == SimulationStatus.COMPLETED
            ));
        }
        return result;
    }
    ///////////////////////////////////////////////////////////////

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

    @Override
    public Simulation findOwnedSimulation(Long applicantUserId, Long simulationId) {
        userService.validateRole(applicantUserId, AuthorityRole.APPLICANT);
        Applicant applicant = applicantService.findByUserId(applicantUserId);

        Simulation sim = simulationRepository.findById(simulationId).orElse(null);
        if (sim == null) {
            throw new ResourceNotFoundException("Simulation id: " + simulationId + " not found");
        }
        if (sim.getApplicant() == null || !sim.getApplicant().getId().equals(applicant.getId())) {
            throw new ForbiddenException(
                    "Simulation id: " + simulationId + " does not belong to applicant");
        }
        return sim;
    }
}
