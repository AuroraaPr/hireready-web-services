package com.hireready.controllers;

import com.hireready.dtos.ApplicantDashboardResponseDTO;
import com.hireready.dtos.CompanyDashboardResponseDTO;
import com.hireready.dtos.DashboardResponseDTO;
import com.hireready.services.DashboardService;
import com.hireready.services.UserService;
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
    @Autowired
    private UserService userService;

    // US24  GET http://localhost:8080/hireready/admin/dashboard
    @GetMapping("/admin/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboard() {
        Long adminUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(dashboardService.getMetrics(adminUserId), HttpStatus.OK);
    }

    // US22  GET http://localhost:8080/hireready/applicants/me/dashboard
    @GetMapping("/applicants/me/dashboard")
    public ResponseEntity<ApplicantDashboardResponseDTO> getApplicantDashboard() {
        Long applicantUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                dashboardService.getApplicantMetrics(applicantUserId), HttpStatus.OK);
    }

    // US23  GET http://localhost:8080/hireready/companies/me/dashboard
    @GetMapping("/companies/me/dashboard")
    public ResponseEntity<CompanyDashboardResponseDTO> getCompanyDashboard() {
        Long companyUserId = userService.getAuthenticatedUserId();
        return new ResponseEntity<>(
                dashboardService.getCompanyMetrics(companyUserId), HttpStatus.OK);
    }
}
