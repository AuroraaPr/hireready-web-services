package com.hireready.controllers;

import com.hireready.dtos.CompanyDTO;
import com.hireready.entities.Company;
import com.hireready.serviceimpl.CompanyServiceImpl;
import com.hireready.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<Company> signUp(@RequestBody CompanyDTO dto) {
        return new ResponseEntity<>(companyService.register(dto), HttpStatus.CREATED);
    }
}