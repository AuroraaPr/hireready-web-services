package com.hireready.controllers;

import com.hireready.dtos.LoginRequestDTO;
import com.hireready.dtos.LoginResponseDTO;
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
}