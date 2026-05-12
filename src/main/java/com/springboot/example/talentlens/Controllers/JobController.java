package com.springboot.example.talentlens.Controllers;

import com.springboot.example.talentlens.DTOs.*;
import com.springboot.example.talentlens.Recruiter.JobSignal;
import com.springboot.example.talentlens.Services.JobService;
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
@RequestMapping("/api/jobs")
@Tag(name = "Recruiter Intelligence" , description = "Endpoints for managing jobs, AI-generated job drafts, and standardized assessments for the shortlisted candidates , decision-making with the help of gemini ON SELECTING THE CANDIDATE")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(
            summary = "Create a new job",
            description = "Allows recruiters to create a new job posting",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<JobSignal> createJob(@Valid @RequestBody JobRequestDto request) {
        return new ResponseEntity<>(jobService.createJob(request), HttpStatus.CREATED);
    }

    @PostMapping("/ai-draft")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(
            summary = "Generate AI job draft",
            description = "Generates an AI-powered professional job description to be autofilled in the text field",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<JobGenerationResponseDto> generateJobDraft(@RequestBody JobGenerationRequestDto request) {
        return new ResponseEntity<>(jobService.generateJobDraft(request), HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get all published jobs",
            description = "Returns all published available jobs in a unfiltered"
    )
    public ResponseEntity<List<JobSignal>> getAllJobs() {
        return new ResponseEntity<>(jobService.getAllPublishedJobs(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get job details",
            description = "Retrieve detailed information about a specific job"
    )
    public ResponseEntity<JobSignal> getJobDetails(@PathVariable Long id) {
        return new ResponseEntity<>(jobService.getJobDetails(id), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(
            summary = "Update job",
            description = "Allows recruiters to update an existing job",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<JobSignal> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequestDto request) {
        return new ResponseEntity<>(jobService.updateJob(id, request), HttpStatus.OK);
    }


    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(
            summary = "Close a job",
            description = "Marks a job as closed and unavailable",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ResponseMessage> closeJob(@PathVariable Long id) {
        return new ResponseEntity<>(jobService.closeJob(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/standardized-assessment")
    @PreAuthorize("hasRole('RECRUITER')")
    @Operation(
            summary = "Generate standardized assessment",
            description = "Generates a technical assessment for a specific job role",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TechnicalGenResponseDto> generateAssessment(
            @PathVariable Long id,
            @RequestBody TechnicalGenRequestDto request) {

        TechnicalGenResponseDto response = jobService.generateStandardizedAssessment(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
