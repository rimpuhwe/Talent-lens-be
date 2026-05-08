package com.springboot.example.talentlens.Evidence;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EvidenceSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private EvidenceModule evidenceModule;

    @Column(columnDefinition = "TEXT")
    private String candidateAnswer;


    private Double score;

    @Column(columnDefinition = "TEXT")
    private String feedback;
}
