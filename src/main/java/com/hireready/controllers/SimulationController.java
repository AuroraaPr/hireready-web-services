package com.hireready.controllers;

import com.hireready.dtos.ContinueSimulationResponseDTO;
import com.hireready.dtos.SimulationResponseDTO;
import com.hireready.dtos.SimulationStartRequestDTO;
import com.hireready.services.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hireready")
@CrossOrigin("*")
public class SimulationController {
    @Autowired
    SimulationService simulationService;

    // US09  POST http://localhost:8080/hireready/applicants/{userId}/simulations
    @PostMapping("/applicants/{userId}/simulations")
    public ResponseEntity<SimulationResponseDTO> start(
            @PathVariable("userId") Long applicantUserId,
            @RequestBody SimulationStartRequestDTO request) {
        return new ResponseEntity<>(
                simulationService.start(applicantUserId, request), HttpStatus.CREATED);
    }

    // US10  GET http://localhost:8080/hireready/applicants/{userId}/simulations/current
    @GetMapping("/applicants/{userId}/simulations/current")
    public ResponseEntity<ContinueSimulationResponseDTO> continueLatest(
            @PathVariable("userId") Long applicantUserId) {
        return new ResponseEntity<>(
                simulationService.continueLatest(applicantUserId), HttpStatus.OK);
    }
}
