package com.hireready.services;

import com.hireready.dtos.CreateQuestionBankRequest;
import com.hireready.dtos.QuestionBankListResponse;
import com.hireready.entities.*;
import com.hireready.repositories.CareerRepository;
import com.hireready.repositories.QuestionBankRepository;
import com.hireready.repositories.SimulationRepository;
import java.util.ArrayList;
import java.util.List;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private SimulationRepository simulationRepository;

    // US06 - Crear banco de preguntas
    @Transactional
    public QuestionBank createQuestionBank(CreateQuestionBankRequest request) {

        //  VALIDACIoN 1: preguntas obligatorias
        if (request.getQuestions() == null || request.getQuestions().isEmpty()) {
            throw new ValidationException("Debe haber al menos una pregunta");
        }

        //  VALIDACION 2: careerIds obligatorios
        if (request.getCareerIds() == null || request.getCareerIds().isEmpty()) {
            throw new ValidationException("Debe haber al menos una carrera");
        }

        // OBTENER CAREERS
        List<Career> careers = careerRepository.findAllById(request.getCareerIds());

        //VALIDACION 3: todas las careers existen
        if (careers.size() != request.getCareerIds().size()) {
            throw new ValidationException("Una o más carreras no existen");
        }

        //CREAR QUESTION BANK
        QuestionBank qb = new QuestionBank();
        qb.setTitle(request.getTitle());
        qb.setJobPosition(request.getJobPosition());
        qb.setLevel(request.getLevel());

        // CLAVE: agregar careers correctamente
        qb.getCareers().addAll(careers);

        //CREAR QUESTIONS
        List<Question> questions = request.getQuestions().stream().map(q -> {
            Question question = new Question();
            question.setContent(q.getContent());
            question.setOrderIndex(q.getOrderIndex());
            question.setQuestionBank(qb);
            return question;
        }).toList();

        qb.setQuestions(questions);

        // GUARDAR TODO
        QuestionBank saved = questionBankRepository.save(qb);

        // recargar desde BD para traer relaciones
        return questionBankRepository.findById(saved.getId()).orElseThrow();
    }

    //US07 - Listar bancos
    public List<QuestionBank> getAllQuestionBanks() {
        return questionBankRepository.findAll();
    }

    public List<QuestionBankListResponse> listForApplicant() {

        return questionBankRepository.findAll().stream().map(qb -> {

            QuestionBankListResponse res = new QuestionBankListResponse();

            res.setId(qb.getId());
            res.setTitle(qb.getTitle());
            res.setJobPosition(qb.getJobPosition());
            res.setLevel(qb.getLevel());

            var simulations = simulationRepository.findByQuestionBankId(qb.getId());

            if (simulations.isEmpty()) {
                res.setStatus("NOT_STARTED");
                res.setActions(java.util.List.of("Iniciar"));
            } else {
                boolean allCompleted = simulations.stream().allMatch(Simulation::isCompleted);

                if (allCompleted) {
                    res.setStatus("COMPLETED");
                    res.setActions(java.util.List.of("Iniciar"));
                } else {
                    res.setStatus("IN_PROGRESS");
                    res.setActions(java.util.List.of("Continuar", "Iniciar"));
                }
            }

            res.setQuestionCount(qb.getQuestions().size());
            res.setCompanyName("Sin empresa");

            return res;

        }).toList();

    }
}