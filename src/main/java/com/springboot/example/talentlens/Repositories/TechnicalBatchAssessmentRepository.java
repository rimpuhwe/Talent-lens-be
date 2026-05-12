package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.Candidate.TechnicalBatchAssessment;
import com.springboot.example.talentlens.Recruiter.JobSignal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnicalBatchAssessmentRepository extends JpaRepository<TechnicalBatchAssessment, Long> {
    Optional<JobSignal> findByJobSignal(JobSignal job);
}