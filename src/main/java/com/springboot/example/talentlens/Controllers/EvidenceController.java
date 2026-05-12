package com.springboot.example.talentlens.Controllers;

import com.springboot.example.talentlens.Evidence.EvidenceModule;
import com.springboot.example.talentlens.Evidence.EvidenceSubmission;
import com.springboot.example.talentlens.Services.EvidenceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evidence")
@PreAuthorize("hasRole('CANDIDATE')")
@Tag(name = "Evidence Engine")
public class EvidenceController {

    private final EvidenceService evidenceService;
    public EvidenceController(EvidenceService evidenceService) { this.evidenceService = evidenceService; }

    @PostMapping("/request")
    public ResponseEntity<EvidenceModule> requestModule(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(evidenceService.requestModule(request.get("role"), request.get("moduleType")));
    }

    @PostMapping("/submit")
    public ResponseEntity<EvidenceSubmission> submitEvidence(@RequestBody Map<String, Object> request) {
        Long moduleId = Long.valueOf(request.get("moduleId").toString());
        String answer = request.get("answer").toString();
        return ResponseEntity.ok(evidenceService.submitEvidence(moduleId, answer));
    }

    @GetMapping("/my-results")
    public ResponseEntity<List<EvidenceSubmission>> getMyResults() {
        return ResponseEntity.ok(evidenceService.getMyResults());
    }

    @GetMapping("/role-scores")
    public ResponseEntity<Map<String, Double>> getRoleScores() {
        return ResponseEntity.ok(evidenceService.getRoleScores());
    }
}