package com.hireready.controllers;

import com.hireready.dtos.ApplicantResponseDTO;
import com.hireready.dtos.ApplicantSummaryResponseDTO;
import com.hireready.dtos.RegisterApplicantRequestDTO;
import com.hireready.dtos.ApplicantUpdateDTO;
import com.hireready.entities.Applicant;
import com.hireready.serviceimpl.ApplicantServiceImpl;
import com.hireready.services.ApplicantService;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${app.frontend.url}")
@RequestMapping("/hireready")
public class ApplicantController {
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private UserService userService;

    // US01  POST http://localhost:8080/hireready/applicants (público)
    @PostMapping("/applicants")
    public ResponseEntity<ApplicantResponseDTO> register(@RequestBody RegisterApplicantRequestDTO request) {
        ApplicantResponseDTO response = applicantService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // US05  GET http://localhost:8080/hireready/applicants/me
    @GetMapping("/applicants/me")
    public ResponseEntity<ApplicantResponseDTO> getProfile() {
        Long userId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(applicantService.getProfile(userId), HttpStatus.OK);
    }

    // US05  PUT http://localhost:8080/hireready/applicants/me
    @PutMapping("/applicants/me")
    public ResponseEntity<ApplicantResponseDTO> updateProfile(@RequestBody ApplicantUpdateDTO request) {
        Long userId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(applicantService.updateProfile(userId, request), HttpStatus.OK);
    }

    // US17  GET http://localhost:8080/hireready/admin/applicants
    @GetMapping("/admin/applicants")
    public ResponseEntity<List<ApplicantSummaryResponseDTO>> listAll() {
        return new ResponseEntity<>(applicantService.listAll(), HttpStatus.OK);
    }
}
