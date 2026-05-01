package com.hireready.services;

import com.hireready.dtos.CreateQuestionBankRequest;
import com.hireready.entities.Career;
import com.hireready.entities.Question;
import com.hireready.entities.QuestionBank;
import com.hireready.repositories.CareerRepository;
import com.hireready.repositories.QuestionBankRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private CareerRepository careerRepository;

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
}