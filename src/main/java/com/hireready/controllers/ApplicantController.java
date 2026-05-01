package com.hireready.controllers;

import com.hireready.dtos.ApplicantDTO;
import com.hireready.entities.Applicant;
import com.hireready.services.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applicants")
public class ApplicantController {
    @Autowired
    private ApplicantService applicantService;

    @PostMapping("/register")
    public ResponseEntity<Applicant> signUp(@RequestBody ApplicantDTO dto) {
        return new ResponseEntity<>(applicantService.register(dto), HttpStatus.CREATED);
    }
}
