package com.hireready.controllers;

import com.hireready.dtos.QuestionBankResponseDTO;
import com.hireready.dtos.CreateQuestionBankRequestDTO;
import com.hireready.dtos.QuestionBankSummaryResponseDTO;
import com.hireready.entities.QuestionBank;
import com.hireready.services.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/hireready-banks")
public class QuestionBankController {

    @Autowired
    QuestionBankService questionBankService;

    // US-06  POST http://localhost:8080/hireready/companies/{userId}/question-banks
    @PostMapping("/companies/{userId}/question-banks")
    public ResponseEntity<QuestionBankResponseDTO> create(
            @PathVariable("userId") Long companyUserId,
            @RequestBody CreateQuestionBankRequestDTO createQuestionBankRequestDTO) {
        return new ResponseEntity<>(
                questionBankService.create(companyUserId, createQuestionBankRequestDTO), HttpStatus.CREATED);
    }

    // US07 y US08
    // GET http://localhost:8080/hireready/applicants/{userId}/question-banks?filter=all
    // GET http://localhost:8080/hireready/applicants/{userId}/question-banks?filter=recommended
    @GetMapping("/applicants/{userId}/question-banks")
    public ResponseEntity<List<QuestionBankSummaryResponseDTO>> listAvailable(
            @PathVariable("userId") Long applicantUserId,
            @RequestParam(value = "filter", defaultValue = "all") String filter) {
        return new ResponseEntity<>(
                questionBankService.listAvailableForApplicant(applicantUserId, filter), HttpStatus.OK);
    }
}
