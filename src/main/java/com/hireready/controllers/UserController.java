package com.hireready.controllers;

import com.hireready.dtos.LoginRequestDTO;
import com.hireready.dtos.TokenResponseDTO;
import com.hireready.dtos.UserStatusResponseDTO;
import com.hireready.entities.User;
import com.hireready.securities.JwtUtilService;
import com.hireready.securities.UserSecurity;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${app.frontend.url}")
@RequestMapping("/hireready")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtilService jwtUtilService;

    // US03  POST http://localhost:8080/hireready/login
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserSecurity userSecurity = (UserSecurity) userDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtUtilService.generateToken(userSecurity);
        User u = userSecurity.getUser();
        String name;
        if (u.getApplicant() != null) {
            name = u.getApplicant().getName();
        } else if (u.getCompany() != null) {
            name = u.getCompany().getName();
        } else {
            name = "Administrador";
        }
        return new ResponseEntity<>(new TokenResponseDTO(
                jwt,
                u.getId(),
                u.getEmail(),
                name,
                u.getAuthority() != null ? u.getAuthority().getRole() : null,
                u.getApplicant() != null ? u.getApplicant().getId() : null,
                u.getCompany()   != null ? u.getCompany().getId()   : null
        ), HttpStatus.OK);
    }

    // US19  POST http://localhost:8080/hireready/admin/users/{targetUserId}/deactivate
    @PostMapping("/admin/users/{targetUserId}/deactivate")
    public ResponseEntity<UserStatusResponseDTO> deactivate(
            @PathVariable("targetUserId") Long targetUserId) {
        Long adminUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                userService.setEnabled(adminUserId, targetUserId, false), HttpStatus.OK);
    }

    // US19  POST http://localhost:8080/hireready/admin/users/{targetUserId}/activate
    @PostMapping("/admin/users/{targetUserId}/activate")
    public ResponseEntity<UserStatusResponseDTO> activate(
            @PathVariable("targetUserId") Long targetUserId) {
        Long adminUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                userService.setEnabled(adminUserId, targetUserId, true), HttpStatus.OK);
    }
}