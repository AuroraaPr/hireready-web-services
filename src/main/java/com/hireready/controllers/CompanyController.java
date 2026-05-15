package com.hireready.controllers;

import com.hireready.dtos.CompanyResponseDTO;
import com.hireready.dtos.CompanySummaryResponseDTO;
import com.hireready.dtos.CompanyUpdateDTO;
import com.hireready.dtos.RegisterCompanyRequestDTO;
import com.hireready.entities.Company;
import com.hireready.serviceimpl.CompanyServiceImpl;
import com.hireready.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${app.frontend.url}")
@RequestMapping("/hireready")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    // US02  POST http://localhost:8080/hireready/companies
    @PostMapping("/companies")
    public ResponseEntity<CompanyResponseDTO> register(@RequestBody RegisterCompanyRequestDTO registerCompanyRequestDTO) {
        return new ResponseEntity<>(companyService.register(registerCompanyRequestDTO), HttpStatus.CREATED);
    }

    // US05  GET http://localhost:8080/hireready/companies/{userId}
    @GetMapping("/companies/{userId}")
    public ResponseEntity<CompanyResponseDTO> getProfile(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(companyService.getProfile(userId), HttpStatus.OK);
    }

    // US05  PUT http://localhost:8080/hireready/companies/{userId}
    @PutMapping("/companies/{userId}")
    public ResponseEntity<CompanyResponseDTO> updateProfile(
            @PathVariable("userId") Long userId,
            @RequestBody CompanyUpdateDTO companyUpdateDTO) {
        return new ResponseEntity<>(companyService.updateProfile(userId, companyUpdateDTO), HttpStatus.OK);
    }

    // US18  GET http://localhost:8080/hireready/admin/{userId}/companies
    @GetMapping("/admin/{userId}/companies")
    public ResponseEntity<List<CompanySummaryResponseDTO>> listAll(
            @PathVariable("userId") Long adminUserId) {
        return new ResponseEntity<>(companyService.listAll(adminUserId), HttpStatus.OK);
    }
}