package com.hireready.controllers;

import com.hireready.dtos.ApplicantResponseDTO;
import com.hireready.dtos.RegisterApplicantRequestDTO;
import com.hireready.dtos.ApplicantUpdateDTO;
import com.hireready.entities.Applicant;
import com.hireready.serviceimpl.ApplicantServiceImpl;
import com.hireready.services.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/hireready")
public class ApplicantController {
    @Autowired
    private ApplicantService applicantService;

    // US01  POST http://localhost:8080/hireready/applicants
    @PostMapping("/applicants")
    public ResponseEntity<ApplicantResponseDTO> register(@RequestBody RegisterApplicantRequestDTO request) {
        ApplicantResponseDTO response = applicantService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // US05  GET http://localhost:8080/hireready/applicants/{userId}
    @GetMapping("/applicants/{userId}")
    public ResponseEntity<ApplicantResponseDTO> getProfile(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(applicantService.getProfile(userId), HttpStatus.OK);
    }

    // US05  PUT http://localhost:8080/hireready/applicants/{userId}
    @PutMapping("/applicants/{userId}")
    public ResponseEntity<ApplicantResponseDTO> updateProfile(
            @PathVariable("userId") Long userId,
            @RequestBody ApplicantUpdateDTO request) {
        return new ResponseEntity<>(applicantService.updateProfile(userId, request), HttpStatus.OK);
    }
}
