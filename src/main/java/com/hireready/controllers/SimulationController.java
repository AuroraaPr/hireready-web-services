package com.hireready.controllers;

import com.hireready.dtos.SimulationResponseDTO;
import com.hireready.dtos.SimulationResumeDTO;
import com.hireready.dtos.SimulationStartRequestDTO;
import com.hireready.services.SimulationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulations")
@CrossOrigin("*")
public class SimulationController {
    @Autowired
    private SimulationService simulationService;

    // US09
    // POST /api/simulations/start
    @PostMapping("/start")
    public ResponseEntity<SimulationResponseDTO> startSimulation(
        @Valid
        @RequestBody
        SimulationStartRequestDTO requestDTO) {

        // id provisional
        Long applicantId = 1L;

        SimulationResponseDTO simulationResponseDTO = simulationService.startSimulation(applicantId, requestDTO);
        return new ResponseEntity<>(simulationResponseDTO, HttpStatus.CREATED);
    }

    // US10
    // GET /api/simulations/me/active
    @GetMapping("/me/active")
    public ResponseEntity<SimulationResumeDTO> getActiveSimulation() {

        // id provisional
        Long applicantId = 1L;

        SimulationResumeDTO simulationResumeDTO = simulationService.getActiveSimulationState(applicantId);
        return new ResponseEntity<>(simulationResumeDTO, HttpStatus.OK);
    }
}
