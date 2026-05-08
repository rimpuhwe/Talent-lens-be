package com.springboot.example.talentlens.Evidence;

import com.springboot.example.talentlens.Candidate.CandidateProfile;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EvidenceModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidate;

    private String targetRole;
    private String moduleType;

    @Column(columnDefinition = "TEXT")
    private String generatedQuestion;

    private boolean isSubmitted = false;
}