package com.springboot.example.talentlens.Admin;

import com.springboot.example.talentlens.Candidate.Candidate;
import com.springboot.example.talentlens.DTOs.RecruiterStatusRequest;
import com.springboot.example.talentlens.DTOs.ResponseMessage;
import com.springboot.example.talentlens.Recruiter.Recruiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/recruiters")
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return new ResponseEntity<>(adminService.getAllRecruiters(), HttpStatus.OK);
    }

    @GetMapping("/recruiters/{id}")
    public ResponseEntity<Recruiter> getRecruiterDetails(@PathVariable Long id) {
        return new ResponseEntity<>(adminService.getRecruiterDetails(id), HttpStatus.OK);
    }

    @PatchMapping("/recruiters/{id}/status")
    public ResponseEntity<ResponseMessage> approveRecruiterRequest(
            @PathVariable Long id,
            @Valid @RequestBody RecruiterStatusRequest request) {
        return new ResponseEntity<>(adminService.updateRecruiterStatus(id, request), HttpStatus.OK);
    }


    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return new ResponseEntity<>(adminService.getAllCandidates(), HttpStatus.OK);
    }
}