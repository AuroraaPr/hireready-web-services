package com.hireready.controllers;

import com.hireready.dtos.ApplicantDTO;
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
@RequestMapping("/api/v1/applicants")
public class ApplicantController {
    @Autowired
    private ApplicantService applicantService;
    @Autowired private ApplicantServiceImpl applicantServiceimpl;

    @PostMapping("/register")
    public ResponseEntity<Applicant> signUp(@RequestBody ApplicantDTO dto) {
        return new ResponseEntity<>(applicantService.register(dto), HttpStatus.CREATED);
    }
    @PutMapping("/me") //CON ESTO PODEMOS ACTUALIZAR PERFIL
    public ResponseEntity<Applicant> updateProfile(@RequestBody ApplicantUpdateDTO dto, Authentication auth) {
        return ResponseEntity.ok(applicantServiceimpl.updateMe(dto, auth));
    }
}
