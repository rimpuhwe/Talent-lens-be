package com.springboot.example.talentlens.Services;

import com.springboot.example.talentlens.DTOs.JobRequestDto;
import com.springboot.example.talentlens.DTOs.ResponseMessage;
import com.springboot.example.talentlens.Enums.JobStatus;
import com.springboot.example.talentlens.Recruiter.JobSignal;
import com.springboot.example.talentlens.Recruiter.Recruiter;
import com.springboot.example.talentlens.Repositories.JobSignalRepository;
import com.springboot.example.talentlens.Repositories.RecruiterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class JobService {

    private final JobSignalRepository jobRepository;
    private final RecruiterRepository recruiterRepository;

    public JobService(JobSignalRepository jobRepository, RecruiterRepository recruiterRepository) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
    }

    private Recruiter getAuthenticatedRecruiter() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return recruiterRepository.findByCompanyEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
    }

    public JobSignal createJob(JobRequestDto request) {
        Recruiter recruiter = getAuthenticatedRecruiter();

        JobSignal job = new JobSignal();
        job.setJobPosition(request.getJobPosition());
        job.setJobDescription(request.getJobDescription());
        job.setSkillsNeeded(request.getRequiredSkills());
        job.setWorkType(request.getWorkType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setRecruiter(recruiter);

        return jobRepository.save(job);
    }

    public List<JobSignal> getAllPublicJobs(String role, String workType) {
        return jobRepository.findPublicJobs(JobStatus.OPEN, role, workType);
    }

    public JobSignal getJobDetails(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public JobSignal updateJob(Long id, JobRequestDto request) {
        Recruiter recruiter = getAuthenticatedRecruiter();
        JobSignal job = getJobDetails(id);

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("Unauthorized: You can only edit your own jobs");
        }

        job.setJobPosition(request.getJobPosition());
        job.setJobDescription(request.getJobDescription());
        job.setSkillsNeeded(request.getRequiredSkills());
        job.setWorkType(request.getWorkType());
        job.setExperienceLevel(request.getExperienceLevel());

        return jobRepository.save(job);
    }

    public ResponseMessage closeJob(Long id) {
        Recruiter recruiter = getAuthenticatedRecruiter();
        JobSignal job = getJobDetails(id);

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("Unauthorized: You can only close your own jobs");
        }

        job.setJobStatus(JobStatus.CLOSED);
        jobRepository.save(job);

        return ResponseMessage.builder()
                .Status(HttpStatus.OK)
                .Message("Job successfully closed")
                .build();
    }
}
