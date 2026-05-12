package com.springboot.example.talentlens.Controllers;

import com.springboot.example.talentlens.Candidate.CandidateProfile;
import com.springboot.example.talentlens.DTOs.RecruiterStatusRequest;
import com.springboot.example.talentlens.DTOs.ResponseMessage;
import com.springboot.example.talentlens.Recruiter.Recruiter;
import com.springboot.example.talentlens.Services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
@Tag(name = "Admin super" , description = "Administrative endpoints for managing recruiters and candidates")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/recruiters")
    @Operation(
            summary = "Get all recruiters",
            description = "Retrieve all recruiters registered in the platform",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return new ResponseEntity<>(adminService.getAllRecruiters(), HttpStatus.OK);
    }

    @GetMapping("/recruiters/{id}")
    @Operation(
            summary = "Get recruiter details",
            description = "Retrieve detailed information about a specific recruiter",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Recruiter> getRecruiterDetails(@PathVariable Long id) {
        return new ResponseEntity<>(adminService.getRecruiterDetails(id), HttpStatus.OK);
    }

    @PatchMapping("/recruiters/{id}/status")
    @Operation(
            summary = "Update recruiter status",
            description = "Approve, reject, or update the status of a recruiter account",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ResponseMessage> approveRecruiterRequest(
            @PathVariable Long id,
            @Valid @RequestBody RecruiterStatusRequest request) {
        return new ResponseEntity<>(adminService.updateRecruiterStatus(id, request), HttpStatus.OK);
    }


    @GetMapping("/candidates")
    @Operation(
            summary = "Get all candidates",
            description = "Retrieve all candidate profiles registered on the platform",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<CandidateProfile>> getAllCandidates() {
        return new ResponseEntity<>(adminService.getAllCandidates(), HttpStatus.OK);
    }
}