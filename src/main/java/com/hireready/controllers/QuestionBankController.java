package com.hireready.controllers;

import com.hireready.entities.QuestionBank;
import com.hireready.repositories.QuestionBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question-banks")
public class QuestionBankController {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    //US06 Crear banco de preguntas
    @PostMapping
    public QuestionBank createQuestionBank(@RequestBody QuestionBank questionBank){
        return questionBankRepository.save(questionBank);
    }

    //US07 Listar simulaciones (question banks)
    @GetMapping
    public List<QuestionBank> getAllQuestionBanks(){
        return questionBankRepository.findAll();
    }
}
