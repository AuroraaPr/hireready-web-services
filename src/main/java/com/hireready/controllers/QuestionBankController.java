package com.hireready.controllers;

import com.hireready.dtos.QuestionBankAdminSummaryResponseDTO;
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
@CrossOrigin(origins = "${app.frontend.url}")
@RequestMapping("/hireready-banks")
public class QuestionBankController {

    @Autowired
    QuestionBankService questionBankService;

    // US06  POST http://localhost:8080/hireready/companies/{userId}/question-banks
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

    // US20  GET http://localhost:8080/hireready/admin/{userId}/question-banks
    //        GET http://localhost:8080/hireready/admin/{userId}/question-banks?companyId=3
    @GetMapping("/admin/{userId}/question-banks")
    public ResponseEntity<List<QuestionBankAdminSummaryResponseDTO>> listForAdmin(
            @PathVariable("userId") Long adminUserId,
            @RequestParam(value = "companyId", required = false) Long companyId) {
        return new ResponseEntity<>(
                questionBankService.listForAdmin(adminUserId, companyId), HttpStatus.OK);
    }

    // US21  GET http://localhost:8080/hireready/admin/{userId}/question-banks/{bankId}
    @GetMapping("/admin/{userId}/question-banks/{bankId}")
    public ResponseEntity<QuestionBankResponseDTO> findDetailForAdmin(
            @PathVariable("userId") Long adminUserId,
            @PathVariable("bankId") Long bankId) {
        return new ResponseEntity<>(questionBankService.findDetailForAdmin(adminUserId, bankId), HttpStatus.OK);
    }
}
