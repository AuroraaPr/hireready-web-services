package com.hireready.controllers;

import com.hireready.dtos.QuestionBankAdminSummaryResponseDTO;
import com.hireready.dtos.QuestionBankResponseDTO;
import com.hireready.dtos.CreateQuestionBankRequestDTO;
import com.hireready.dtos.QuestionBankSummaryResponseDTO;
import com.hireready.entities.QuestionBank;
import com.hireready.services.QuestionBankService;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${app.frontend.url}")
@RequestMapping("/hireready")
public class QuestionBankController {

    @Autowired
    QuestionBankService questionBankService;
    @Autowired
    private UserService userService;

    // US06  POST http://localhost:8080/hireready/companies/me/question-banks
    @PostMapping("/companies/me/question-banks")
    public ResponseEntity<QuestionBankResponseDTO> create(
            @RequestBody CreateQuestionBankRequestDTO createQuestionBankRequestDTO) {
        Long companyUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                questionBankService.create(companyUserId, createQuestionBankRequestDTO), HttpStatus.CREATED);
    }

    // US07 y US08
    // GET http://localhost:8080/hireready/applicants/me/question-banks?filter=all
    // GET http://localhost:8080/hireready/applicants/me/question-banks?filter=recommended
    @GetMapping("/applicants/me/question-banks")
    public ResponseEntity<List<QuestionBankSummaryResponseDTO>> listForApplicant(
            @RequestParam(value = "filter", defaultValue = "all") String filter) {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                questionBankService.listAvailableForApplicant(applicantUserId, filter), HttpStatus.OK);
    }

    // US20  GET http://localhost:8080/hireready/admin/question-banks
    //        GET http://localhost:8080/hireready/admin/question-banks?companyId=3
    @GetMapping("/admin/question-banks")
    public ResponseEntity<List<QuestionBankAdminSummaryResponseDTO>> listForAdmin(
            @RequestParam(value = "companyId", required = false) Long companyId) {
        Long adminUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                questionBankService.listForAdmin(adminUserId, companyId), HttpStatus.OK);
    }

    // US21  GET http://localhost:8080/hireready/admin/question-banks/{bankId}
    @GetMapping("/admin/question-banks/{bankId}")
    public ResponseEntity<QuestionBankResponseDTO> getByIdForAdmin(
            @PathVariable("bankId") Long bankId) {
        Long adminUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(questionBankService.findDetailForAdmin(adminUserId, bankId), HttpStatus.OK);
    }
}
