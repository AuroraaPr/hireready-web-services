package com.hireready.controllers;

import com.hireready.dtos.*;
import com.hireready.entities.ResponseAudio;
import com.hireready.services.SimulationService;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/hireready")
@CrossOrigin(origins = "${app.frontend.url}")
public class SimulationController {
    @Autowired
    SimulationService simulationService;
    @Autowired
    private UserService userService;

    // US09  POST http://localhost:8080/hireready/applicants/me/simulations
    @PostMapping("/applicants/me/simulations")
    public ResponseEntity<SimulationResponseDTO> start(
            @RequestBody SimulationStartRequestDTO request) {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                simulationService.start(applicantUserId, request), HttpStatus.CREATED);
    }

    // US10  GET http://localhost:8080/hireready/applicants/me/simulations/current
    @GetMapping("/applicants/me/simulations/current")
    public ResponseEntity<ContinueSimulationResponseDTO> continueLatest() {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                simulationService.continueLatest(applicantUserId), HttpStatus.OK);
    }

    // US11  POST http://localhost:8080/hireready/applicants/me/simulations/{simulationId}/responses
    @PostMapping("/applicants/me/simulations/{simulationId}/responses")
    public ResponseEntity<SubmitResponseResponseDTO> submitResponse(
            @PathVariable("simulationId") Long simulationId,
            @RequestParam("questionId") Long questionId,
            @RequestParam("duration") Integer duration,
            @RequestParam("audio") MultipartFile audio) {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                simulationService.submitResponse(applicantUserId, simulationId, questionId, duration, audio),
                HttpStatus.CREATED);
    }

    // US12  POST http://localhost:8080/hireready/applicants/me/simulations/{simulationId}/exit
    @PostMapping("/applicants/me/simulations/{simulationId}/exit")
    public ResponseEntity<ExitSimulationResponseDTO> exit(
            @PathVariable("simulationId") Long simulationId) {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                simulationService.exit(applicantUserId, simulationId), HttpStatus.OK);
    }

    // US13  POST http://localhost:8080/hireready/applicants/me/simulations/{simulationId}/finalize
    @PostMapping("/applicants/me/simulations/{simulationId}/finalize")
    public ResponseEntity<FinalizeSimulationResponseDTO> finalizeSimulation(
            @PathVariable("simulationId") Long simulationId) {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                simulationService.finalize(applicantUserId, simulationId), HttpStatus.OK);
    }

    // US14  GET http://localhost:8080/hireready/applicants/me/simulations/{simulationId}/report
    @GetMapping("/applicants/me/simulations/{simulationId}/report")
    public ResponseEntity<SimulationReportFullResponseDTO> getReport(
            @PathVariable("simulationId") Long simulationId) {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                simulationService.getReport(applicantUserId, simulationId), HttpStatus.OK);
    }

    // US14  GET http://localhost:8080/hireready/applicants/me/simulations/{simulationId}/responses/{responseId}/audio
    @GetMapping("/applicants/me/simulations/{simulationId}/responses/{responseId}/audio")
    public ResponseEntity<byte[]> getResponseAudio(
            @PathVariable("simulationId") Long simulationId,
            @PathVariable("responseId") Long responseId) {
        Long applicantUserId = userService.getAuthenticatedUserId();
        ResponseAudio a = simulationService.getResponseAudio(applicantUserId, simulationId, responseId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(a.getContentType()))
                .body(a.getAudio());
    }

    // US15  GET http://localhost:8080/hireready/applicants/me/simulations/history
    @GetMapping("/applicants/me/simulations/history")
    public ResponseEntity<List<SimulationHistoryItemResponseDTO>> listHistory() {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                simulationService.listHistory(applicantUserId), HttpStatus.OK);
    }
}
