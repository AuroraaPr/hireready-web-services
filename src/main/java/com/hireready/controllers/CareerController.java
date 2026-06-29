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

    // GET http://localhost:8080/hireready/careers (público)
    @GetMapping("/careers")
    public ResponseEntity<List<CareerResponseDTO>> listPublic() {
        return new ResponseEntity<>(careerService.listAll(), HttpStatus.OK);
    }

    // US16  GET http://localhost:8080/hireready/admin/careers
    @GetMapping("/admin/careers")
    public ResponseEntity<List<CareerResponseDTO>> list() {
        return new ResponseEntity<>(careerService.listAll(), HttpStatus.OK);
    }

    // US16  POST http://localhost:8080/hireready/admin/careers
    @PostMapping("/admin/careers")
    public ResponseEntity<CareerResponseDTO> add(@RequestBody CareerRequestDTO request) {
        return new ResponseEntity<>(careerService.create(request), HttpStatus.CREATED);
    }

    // US16  PUT http://localhost:8080/hireready/admin/careers/{careerId}
    @PutMapping("/admin/careers/{careerId}")
    public ResponseEntity<CareerResponseDTO> update(
            @PathVariable("careerId") Long careerId,
            @RequestBody CareerRequestDTO request) {
        return new ResponseEntity<>(
                careerService.update(careerId, request), HttpStatus.OK);
    }
}
