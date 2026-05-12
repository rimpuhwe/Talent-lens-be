package com.springboot.example.talentlens.Candidate;



import com.springboot.example.talentlens.Recruiter.JobSignal;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TechnicalBatchAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobSignal jobSignal;

    private String recruiterFocusAreas;

    @Column(columnDefinition = "TEXT")
    private String technicalQuestion;

    private int timeLimitMinutes;

    @Column(columnDefinition = "TEXT")
    private String gradingRubricJson;
}