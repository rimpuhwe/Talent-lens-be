package com.springboot.example.talentlens.Recruiter;


import com.springboot.example.talentlens.DTOs.SkillSet;
import com.springboot.example.talentlens.Enums.JobStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class JobSignal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String jobPosition;
    private String jobDescription;
    private String workType;
    private String experienceLevel;

    @ElementCollection
    @CollectionTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"))
    private List<SkillSet> skillsNeeded;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus = JobStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;
}
