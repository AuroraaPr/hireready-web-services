package com.hireready.controllers;

import com.hireready.dtos.LoginRequestDTO;
import com.hireready.dtos.LoginResponseDTO;
import com.hireready.dtos.UserStatusResponseDTO;
import com.hireready.entities.User;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/hireready")
public class UserController {
    @Autowired
    private UserService userService;

    // US03  POST http://localhost:8080/hireready/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(userService.loginAsDto(loginRequestDTO), HttpStatus.OK);
    }
    // US19  POST http://localhost:8080/hireready/admin/{userId}/users/{targetUserId}/deactivate
    @PostMapping("/admin/{userId}/users/{targetUserId}/deactivate")
    public ResponseEntity<UserStatusResponseDTO> deactivate(
            @PathVariable("userId") Long adminUserId,
            @PathVariable("targetUserId") Long targetUserId) {
        return new ResponseEntity<>(
                userService.setEnabled(adminUserId, targetUserId, false), HttpStatus.OK);
    }

    // US19  POST http://localhost:8080/hireready/admin/{userId}/users/{targetUserId}/activate
    @PostMapping("/admin/{userId}/users/{targetUserId}/activate")
    public ResponseEntity<UserStatusResponseDTO> activate(
            @PathVariable("userId") Long adminUserId,
            @PathVariable("targetUserId") Long targetUserId) {
        return new ResponseEntity<>(
                userService.setEnabled(adminUserId, targetUserId, true), HttpStatus.OK);
    }
}