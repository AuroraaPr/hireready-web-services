package com.hireready.serviceimpl;

import com.hireready.dtos.QuestionDTO;
import com.hireready.dtos.SimulationResponseDTO;
import com.hireready.dtos.SimulationResumeDTO;
import com.hireready.dtos.SimulationStartRequestDTO;
import com.hireready.entities.*;
import com.hireready.enums.SimulationStatus;
import com.hireready.exceptions.ActiveSimulationExistsException;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.repositories.SimulationRepository;
import com.hireready.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SimulationServiceImpl implements SimulationService {
    @Autowired
    private SimulationRepository simulationRepository;
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ResponseService responseService;

    @Override
    @Transactional
    public SimulationResponseDTO startSimulation(Long applicantId, SimulationStartRequestDTO simulationStartRequestDTO) {

        Simulation activeSimulation = simulationRepository.findByApplicantIdAndStatus(applicantId, SimulationStatus.IN_PROGRESS);

        if (activeSimulation != null) {
            if (!simulationStartRequestDTO.isForceStart()) {
                throw new ActiveSimulationExistsException("Tienes una simulación en progreso. ¿Deseas abandonarla e iniciar una nueva?");
            } else {
                activeSimulation.setStatus(SimulationStatus.ABANDONED);
                activeSimulation.setCompletedAt(LocalDateTime.now());
                simulationRepository.save(activeSimulation);
            }
        }

        Applicant applicant = applicantService.findById(applicantId);
        if (applicant == null) throw new ResourceNotFoundException("Postulante no encontrado");

        QuestionBank questionBank = questionBankService.findById(simulationStartRequestDTO.getQuestionBankId());
        if (questionBank == null) throw new ResourceNotFoundException("Banco de preguntas no encontrado");

        Simulation newSimulation = new Simulation();
        newSimulation.setApplicant(applicant);
        newSimulation.setQuestionBank(questionBank);
        newSimulation.setStartedAt(LocalDateTime.now());
        newSimulation.setStatus(SimulationStatus.IN_PROGRESS);
        newSimulation = simulationRepository.save(newSimulation);

        SimulationResponseDTO response = new SimulationResponseDTO();
        response.setSimulationId(newSimulation.getId());
        response.setQuestionBankId(questionBank.getId());
        response.setStatus(newSimulation.getStatus());
        response.setStartedAt(newSimulation.getStartedAt());
        return response;
    }

    @Override
    @Transactional
    public SimulationResumeDTO getActiveSimulationState(Long applicantId) {

        Simulation activeSimulation = simulationRepository.findByApplicantIdAndStatus(applicantId, SimulationStatus.IN_PROGRESS);
        if (activeSimulation == null) {
            throw new ResourceNotFoundException("No tienes ninguna simulación en progreso.");
        }

        Long questionBankId = activeSimulation.getQuestionBank().getId();

        List<Question> questions = questionService.findByQuestionBankIdOrderByOrderIndexAsc(questionBankId);
        List<Response> responses = responseService.findBySimulationId(activeSimulation.getId());

        Set<Long> answeredQuestionIds = responses.stream()
                .map(r -> r.getQuestion().getId())
                .collect(Collectors.toSet());

        //buscar la primera pregunta pendiente
        Question pendingQuestion = null;
        int currentQuestionNumber = 1;

        for (Question q : questions) {
            if (!answeredQuestionIds.contains(q.getId())) {
                pendingQuestion = q;
                break;
            }
            currentQuestionNumber++;
        }

        // caso: respondió todas las preguntas, pero faltó falto finalizar
        if (pendingQuestion == null) {
            throw new IllegalStateException("Todas las preguntas han sido respondidas. Debes finalizar la simulación.");
        }

        SimulationResumeDTO dto = new SimulationResumeDTO();
        dto.setSimulationId(activeSimulation.getId());
        dto.setQuestionBankId(questionBankId);
        dto.setCurrentQuestionNumber(currentQuestionNumber);
        dto.setTotalQuestions(questions.size());

        QuestionDTO qDto = new QuestionDTO();
        qDto.setId(pendingQuestion.getId());
        qDto.setContent(pendingQuestion.getContent());
        qDto.setOrderIndex(pendingQuestion.getOrderIndex());

        dto.setPendingQuestion(qDto);

        return dto;
    }
}
