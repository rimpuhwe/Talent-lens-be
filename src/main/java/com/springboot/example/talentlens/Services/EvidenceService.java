package com.springboot.example.talentlens.Services;

import com.springboot.example.talentlens.Candidate.CandidateProfile;
import com.springboot.example.talentlens.Evidence.EvidenceModule;
import com.springboot.example.talentlens.Evidence.EvidenceSubmission;
import com.springboot.example.talentlens.Repositories.EvidenceModuleRepository;
import com.springboot.example.talentlens.Repositories.EvidenceSubmissionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvidenceService {

    private final EvidenceModuleRepository moduleRepository;
    private final EvidenceSubmissionRepository submissionRepository;
    private final CandidateService candidateService;
    private final RestTemplate restTemplate;

    // You will set this in application.yaml later (e.g., http://localhost:8000)
    @Value("${PYTHON_AI_SERVICE_URL:http://localhost:8000}")
    private String pythonServiceUrl;

    public EvidenceService(EvidenceModuleRepository moduleRepository,
                           EvidenceSubmissionRepository submissionRepository,
                           CandidateService candidateService) {
        this.moduleRepository = moduleRepository;
        this.submissionRepository = submissionRepository;
        this.candidateService = candidateService;
        this.restTemplate = new RestTemplate();
    }


    public EvidenceModule requestModule(String role, String moduleType) {
        CandidateProfile candidate = candidateService.getMyProfile();

        if (!candidate.isProfileCompleted()) {
            throw new RuntimeException("Profile must be 100% complete to access Evidence Engine");
        }


        Map<String, String> pythonRequest = new HashMap<>();
        pythonRequest.put("role", role);
        pythonRequest.put("moduleType", moduleType);


        Map<String, String> pythonResponse = restTemplate.postForObject(
                pythonServiceUrl + "/generate-module",
                pythonRequest,
                Map.class
        );

        EvidenceModule module = new EvidenceModule();
        module.setCandidate(candidate);
        module.setTargetRole(role);
        module.setModuleType(moduleType);
        module.setGeneratedQuestion(pythonResponse.get("question"));

        return moduleRepository.save(module);
    }

    public EvidenceSubmission submitEvidence(Long moduleId, String answer) {
        EvidenceModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (module.isSubmitted()) {
            throw new RuntimeException("This module has already been submitted");
        }


        Map<String, Object> pythonRequest = new HashMap<>();
        pythonRequest.put("question", module.getGeneratedQuestion());
        pythonRequest.put("answer", answer);
        pythonRequest.put("role", module.getTargetRole());


        Map<String, Object> pythonResponse = restTemplate.postForObject(
                pythonServiceUrl + "/evaluate",
                pythonRequest,
                Map.class
        );

        EvidenceSubmission submission = new EvidenceSubmission();
        submission.setEvidenceModule(module);
        submission.setCandidateAnswer(answer);


        assert pythonResponse != null;
        Object scoreObj = pythonResponse.get("score");
        submission.setScore(scoreObj instanceof Number ? ((Number) scoreObj).doubleValue() : 0.0);
        submission.setFeedback((String) pythonResponse.get("feedback"));

        module.setSubmitted(true);
        moduleRepository.save(module);

        return submissionRepository.save(submission);
    }

    public List<EvidenceSubmission> getMyResults() {
        CandidateProfile candidate = candidateService.getMyProfile();
        return submissionRepository.findByEvidenceModuleCandidate(candidate);
    }

    public Map<String, Double> getRoleScores() {
        CandidateProfile candidate = candidateService.getMyProfile();
        List<EvidenceSubmission> submissions = submissionRepository.findByEvidenceModuleCandidate(candidate);

        Map<String, Double> roleAverages = new HashMap<>();
        Map<String, Integer> roleCounts = new HashMap<>();


        for (EvidenceSubmission sub : submissions) {
            String role = sub.getEvidenceModule().getTargetRole();
            roleAverages.put(role, roleAverages.getOrDefault(role, 0.0) + sub.getScore());
            roleCounts.put(role, roleCounts.getOrDefault(role, 0) + 1);
        }

        roleAverages.replaceAll((r, v) -> roleAverages.get(r) / roleCounts.get(r));

        return roleAverages;
    }
}