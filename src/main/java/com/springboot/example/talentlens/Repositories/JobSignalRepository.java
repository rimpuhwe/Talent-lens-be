package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.Recruiter.JobSignal;
import com.springboot.example.talentlens.Enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobSignalRepository extends JpaRepository<JobSignal, Long> {

    @Query("SELECT j FROM JobSignal j WHERE j.jobStatus = :status " +
            "AND (:role IS NULL OR LOWER(j.jobPosition) LIKE LOWER(CONCAT('%', :role, '%'))) " +
            "AND (:workType IS NULL OR LOWER(j.workType) = LOWER(:workType))")
    List<JobSignal> findPublicJobs(
            @Param("status") JobStatus status,
            @Param("role") String role,
            @Param("workType") String workType
    );
}