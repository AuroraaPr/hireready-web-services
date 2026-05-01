package com.hireready.controllers;

import com.hireready.dtos.CreateQuestionBankRequest;
import com.hireready.entities.QuestionBank;
import com.hireready.entities.Question;
import com.hireready.repositories.QuestionBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question-banks")
public class QuestionBankController {

    @Autowired
    private com.hireready.services.QuestionBankService questionBankService;

    //US06 Crear banco de preguntas
    @PostMapping
    public QuestionBank createQuestionBank(@RequestBody CreateQuestionBankRequest request){
        return questionBankService.createQuestionBank(request);
    }

    //US07 Listar simulaciones (question banks)
    @GetMapping
    public List<QuestionBank> getAllQuestionBanks(){
        return questionBankService.getAllQuestionBanks();
    }
}
