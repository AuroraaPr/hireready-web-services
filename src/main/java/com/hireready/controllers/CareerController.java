package com.hireready.controllers;

import com.hireready.dtos.CareerRequestDTO;
import com.hireready.dtos.CareerResponseDTO;
import com.hireready.services.CareerService;
import com.hireready.services.UserService;
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
    @Autowired
    private UserService userService;

    // US16  GET http://localhost:8080/hireready/admin/careers
    @GetMapping("/admin/careers")
    public ResponseEntity<List<CareerResponseDTO>> listAll() {
        Long adminUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(careerService.listAll(adminUserId), HttpStatus.OK);
    }

    // US16  POST http://localhost:8080/hireready/admin/careers
    @PostMapping("/admin/careers")
    public ResponseEntity<CareerResponseDTO> create(@RequestBody CareerRequestDTO request) {
        Long adminUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(careerService.create(adminUserId, request), HttpStatus.CREATED);
    }

    // US16  PUT http://localhost:8080/hireready/admin/careers/{careerId}
    @PutMapping("/admin/careers/{careerId}")
    public ResponseEntity<CareerResponseDTO> update(
            @PathVariable("careerId") Long careerId,
            @RequestBody CareerRequestDTO request) {
        Long adminUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                careerService.update(adminUserId, careerId, request), HttpStatus.OK);
    }
}
