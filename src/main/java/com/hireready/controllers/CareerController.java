package com.hireready.controllers;

import com.hireready.dtos.CareerRequestDTO;
import com.hireready.dtos.CareerResponseDTO;
import com.hireready.services.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${app.frontend.url}")
@RequestMapping("/hireready")
public class CareerController {
    @Autowired
    CareerService careerService;

    // US16  GET http://localhost:8080/hireready/admin/{userId}/careers
    @GetMapping("/admin/{userId}/careers")
    public ResponseEntity<List<CareerResponseDTO>> listAll(@PathVariable("userId") Long adminUserId) {
        return new ResponseEntity<>(careerService.listAll(adminUserId), HttpStatus.OK);
    }

    // US16  POST http://localhost:8080/hireready/admin/{userId}/careers
    @PostMapping("/admin/{userId}/careers")
    public ResponseEntity<CareerResponseDTO> create(
            @PathVariable("userId") Long adminUserId,
            @RequestBody CareerRequestDTO request) {
        return new ResponseEntity<>(careerService.create(adminUserId, request), HttpStatus.CREATED);
    }

    // US16  PUT http://localhost:8080/hireready/admin/{userId}/careers/{careerId}
    @PutMapping("/admin/{userId}/careers/{careerId}")
    public ResponseEntity<CareerResponseDTO> update(
            @PathVariable("userId") Long adminUserId,
            @PathVariable("careerId") Long careerId,
            @RequestBody CareerRequestDTO request) {
        return new ResponseEntity<>(
                careerService.update(adminUserId, careerId, request), HttpStatus.OK);
    }
}
