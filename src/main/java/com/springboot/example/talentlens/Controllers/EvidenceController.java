package com.springboot.example.talentlens.Controllers;

import com.springboot.example.talentlens.DTOs.EvidenceRequest;
import com.springboot.example.talentlens.DTOs.EvidenceSubmissionRequest;
import com.springboot.example.talentlens.Evidence.EvidenceModule;
import com.springboot.example.talentlens.Evidence.EvidenceSubmission;
import com.springboot.example.talentlens.Services.EvidenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evidence")
@PreAuthorize("hasRole('CANDIDATE')")
@Tag(name = "Evidence Engine" , description = "APIs for requesting assessment modules, submitting evidence, and viewing candidate evidence results")
public class EvidenceController {

    private final EvidenceService evidenceService;
    public EvidenceController(EvidenceService evidenceService) { this.evidenceService = evidenceService; }

    @PostMapping("/request")
    @Operation(
            summary = "Request evidence module",
            description = "Generates or retrieves an evidence module for a specific role(candidate's selected role) and module type. \"A\": \"a practical, real-world skill mission (e.g., data analysis, coding, financial modeling)\",\n" +
                    "        \"B\": \"an ethical, communication, or professional dilemma requiring judgment\",\n" +
                    "        \"C\": \"a learning agility exercise where you introduce a novel concept and ask them to apply it\",\n" +
                    "        \"D\": \"a communication prompt requiring a structured, stakeholder-facing response\""
    )
    public ResponseEntity<EvidenceModule> requestModule(@Valid @RequestBody EvidenceRequest request) {
        return ResponseEntity.ok(evidenceService.requestModule(request.getRole(), request.getModuleType()));
    }

    @PostMapping("/submit")
    @Operation(
            summary = "Submit evidence",
            description = "Submits candidate evidence or answers for a specific evidence module"
    )
    public ResponseEntity<EvidenceSubmission> submitEvidence(@Valid @RequestBody EvidenceSubmissionRequest request) {

        // No more ugly casting or .toString() parsing!
        return ResponseEntity.ok(evidenceService.submitEvidence(request.getModuleId(), request.getAnswer()));
    }
    @GetMapping("/my-results")
    @Operation(
            summary = "Get my evidence results",
            description = "Returns all evidence submissions with the feedback generated"
    )
    public ResponseEntity<List<EvidenceSubmission>> getMyResults() {
        return ResponseEntity.ok(evidenceService.getMyResults());
    }

    @GetMapping("/role-scores")
    @Operation(
            summary = "Get role scores",
            description = "Returns calculated scores grouped by candidate roles"
    )
    public ResponseEntity<Map<String, Double>> getRoleScores() {
        return ResponseEntity.ok(evidenceService.getRoleScores());
    }
}