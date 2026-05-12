package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.Candidate.CandidateProfile;
import com.springboot.example.talentlens.Evidence.EvidenceSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceSubmissionRepository extends JpaRepository<EvidenceSubmission, Long> {

    List<EvidenceSubmission> findByEvidenceModuleCandidate(CandidateProfile candidate);

    List<EvidenceSubmission> findByEvidenceModuleCandidateAndEvidenceModuleTargetRole(CandidateProfile candidate, String targetRole);
}