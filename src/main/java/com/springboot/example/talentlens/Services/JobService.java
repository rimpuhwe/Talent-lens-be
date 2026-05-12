package com.springboot.example.talentlens.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.example.talentlens.Candidate.TechnicalBatchAssessment;
import com.springboot.example.talentlens.DTOs.*;
import com.springboot.example.talentlens.Enums.JobStatus;
import com.springboot.example.talentlens.Recruiter.JobSignal;
import com.springboot.example.talentlens.Recruiter.Recruiter;
import com.springboot.example.talentlens.Repositories.JobSignalRepository;
import com.springboot.example.talentlens.Repositories.RecruiterRepository;
import com.springboot.example.talentlens.Repositories.TechnicalBatchAssessmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class JobService {

    private final JobSignalRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final TechnicalBatchAssessmentRepository assessmentRepository;

    @Value("${PYTHON_AI_SERVICE_URL:http://localhost:8000}")
    private String pythonServiceUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public JobService(JobSignalRepository jobRepository, RecruiterRepository recruiterRepository , TechnicalBatchAssessmentRepository assessmentRepository ) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.assessmentRepository = assessmentRepository;
    }

    private Recruiter getAuthenticatedRecruiter() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return recruiterRepository.findByCompanyEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
    }

    public JobGenerationResponseDto generateJobDraft(JobGenerationRequestDto request) {
        Recruiter recruiter = getAuthenticatedRecruiter();

        if (request.getCompanyAbout() == null || request.getCompanyAbout().isBlank()) {
            request.setCompanyAbout(recruiter.getCompanyDescription());
        }

        try {
            // MUST be JobGenerationResponseDto.class here!
            JobGenerationResponseDto aiResponse = restTemplate.postForObject(
                    pythonServiceUrl + "/api/recruiter/generate-job",
                    request,
                    JobGenerationResponseDto.class
            );

            if (aiResponse == null) {
                throw new RuntimeException("Failed to generate job draft from AI.");
            }

            return aiResponse;

        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            throw new RuntimeException("Python AI Error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Integration Error: " + e.getMessage());
        }
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

    public List<JobSignal> getAllPublishedJobs() {
        return jobRepository.findAll();
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

        return new  ResponseMessage(HttpStatus.OK , "Job successfully closed");
    }
    public TechnicalGenResponseDto generateStandardizedAssessment(Long jobId, TechnicalGenRequestDto request) {
        JobSignal job = getJobDetails(jobId);

        // Ensure we don't accidentally overwrite an existing active test for this job
        if (assessmentRepository.findByJobSignal(job).isPresent()) {
            throw new RuntimeException("A standardized assessment already exists for this job.");
        }

        // Call Python AI Microservice
        TechnicalGenResponseDto pythonResponse = restTemplate.postForObject(
                pythonServiceUrl + "/api/assessment/generate-standardized",
                request,
                TechnicalGenResponseDto.class
        );

        if (pythonResponse == null) {
            throw new RuntimeException("Failed to generate assessment from AI Engine.");
        }

        // Save to Database so all candidates fetch THIS exact test
        TechnicalBatchAssessment assessment = new TechnicalBatchAssessment();
        assessment.setJobSignal(job);
        assessment.setRecruiterFocusAreas(request.getRecruiter_focus_areas());
        assessment.setTechnicalQuestion(pythonResponse.getTechnical_question());
        assessment.setTimeLimitMinutes(pythonResponse.getRecommended_time_limit_minutes());

        // Convert rubric list to a JSON string for easy storage (you can use ObjectMapper here)
        try {
            ObjectMapper mapper = new ObjectMapper();
            assessment.setGradingRubricJson(mapper.writeValueAsString(pythonResponse.getGrading_rubric()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse rubric JSON.");
        }

        assessmentRepository.save(assessment);

        return pythonResponse;
    }
}
