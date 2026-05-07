package com.springboot.example.talentlens.Controllers;

import com.springboot.example.talentlens.DTOs.JobRequestDto;
import com.springboot.example.talentlens.DTOs.ResponseMessage;
import com.springboot.example.talentlens.Recruiter.JobSignal;
import com.springboot.example.talentlens.Services.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobSignal> createJob(@Valid @RequestBody JobRequestDto request) {
        return new ResponseEntity<>(jobService.createJob(request), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<JobSignal>> getAllJobs(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String workType) {
        return new ResponseEntity<>(jobService.getAllPublicJobs(role, workType), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<JobSignal> getJobDetails(@PathVariable Long id) {
        return new ResponseEntity<>(jobService.getJobDetails(id), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobSignal> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequestDto request) {
        return new ResponseEntity<>(jobService.updateJob(id, request), HttpStatus.OK);
    }


    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ResponseMessage> closeJob(@PathVariable Long id) {
        return new ResponseEntity<>(jobService.closeJob(id), HttpStatus.OK);
    }
}
