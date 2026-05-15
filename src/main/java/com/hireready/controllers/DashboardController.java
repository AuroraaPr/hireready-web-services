package com.hireready.controllers;

import com.hireready.dtos.ApplicantDashboardResponseDTO;
import com.hireready.dtos.CompanyDashboardResponseDTO;
import com.hireready.dtos.DashboardResponseDTO;
import com.hireready.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${app.frontend.url}")
@RequestMapping("/hireready")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;

    // US24  GET http://localhost:8080/hireready/admin/{userId}/dashboard
    @GetMapping("/admin/{userId}/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboard(@PathVariable("userId") Long adminUserId) {
        return new ResponseEntity<>(dashboardService.getMetrics(adminUserId), HttpStatus.OK);
    }

    // US22  GET http://localhost:8080/hireready/applicants/{userId}/dashboard
    @GetMapping("/applicants/{userId}/dashboard")
    public ResponseEntity<ApplicantDashboardResponseDTO> getApplicantDashboard(
            @PathVariable("userId") Long applicantUserId) {
        return new ResponseEntity<>(
                dashboardService.getApplicantMetrics(applicantUserId), HttpStatus.OK);
    }

    // US23  GET http://localhost:8080/hireready/companies/{userId}/dashboard
    @GetMapping("/companies/{userId}/dashboard")
    public ResponseEntity<CompanyDashboardResponseDTO> getCompanyDashboard(
            @PathVariable("userId") Long companyUserId) {
        return new ResponseEntity<>(
                dashboardService.getCompanyMetrics(companyUserId), HttpStatus.OK);
    }
}
