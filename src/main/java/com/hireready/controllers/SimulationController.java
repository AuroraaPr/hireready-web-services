package com.hireready.controllers;

import com.hireready.dtos.*;
import com.hireready.services.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hireready")
@CrossOrigin(origins = "${app.frontend.url}")
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

    // US11  POST http://localhost:8080/hireready/applicants/{userId}/simulations/{simulationId}/responses
    @PostMapping("/applicants/{userId}/simulations/{simulationId}/responses")
    public ResponseEntity<SubmitResponseResponseDTO> submitResponse(
            @PathVariable("userId") Long applicantUserId,
            @PathVariable("simulationId") Long simulationId,
            @RequestBody SubmitResponseRequestDTO submitResponseRequestDTO) {
        return new ResponseEntity<>(
                simulationService.submitResponse(applicantUserId, simulationId, submitResponseRequestDTO),
                HttpStatus.CREATED);
    }

    // US12  POST http://localhost:8080/hireready/applicants/{userId}/simulations/{simulationId}/exit
    @PostMapping("/applicants/{userId}/simulations/{simulationId}/exit")
    public ResponseEntity<ExitSimulationResponseDTO> exit(
            @PathVariable("userId") Long applicantUserId,
            @PathVariable("simulationId") Long simulationId) {
        return new ResponseEntity<>(
                simulationService.exit(applicantUserId, simulationId), HttpStatus.OK);
    }

    // US13  POST http://localhost:8080/hireready/applicants/{userId}/simulations/{simulationId}/finalize
    @PostMapping("/applicants/{userId}/simulations/{simulationId}/finalize")
    public ResponseEntity<FinalizeSimulationResponseDTO> finalizeSimulation(
            @PathVariable("userId") Long applicantUserId,
            @PathVariable("simulationId") Long simulationId) {
        return new ResponseEntity<>(
                simulationService.finalize(applicantUserId, simulationId), HttpStatus.OK);
    }

    // US14  GET http://localhost:8080/hireready/applicants/{userId}/simulations/{simulationId}/report
    @GetMapping("/applicants/{userId}/simulations/{simulationId}/report")
    public ResponseEntity<SimulationReportFullResponseDTO> getReport(
            @PathVariable("userId") Long applicantUserId,
            @PathVariable("simulationId") Long simulationId) {
        return new ResponseEntity<>(
                simulationService.getReport(applicantUserId, simulationId), HttpStatus.OK);
    }

    // US15  GET http://localhost:8080/hireready/applicants/{userId}/simulations/history
    @GetMapping("/applicants/{userId}/simulations/history")
    public ResponseEntity<List<SimulationHistoryItemResponseDTO>> listHistory(
            @PathVariable("userId") Long applicantUserId) {
        return new ResponseEntity<>(
                simulationService.listHistory(applicantUserId), HttpStatus.OK);
    }
}
