package com.springboot.example.talentlens.Services;

import com.springboot.example.talentlens.Candidate.CandidateProfile;
import com.springboot.example.talentlens.DTOs.RecruiterStatusRequest;
import com.springboot.example.talentlens.DTOs.ResponseMessage;
import com.springboot.example.talentlens.Enums.AIExtractionStatus;
import com.springboot.example.talentlens.Recruiter.Recruiter;
import com.springboot.example.talentlens.Repositories.CandidateRepository;
import com.springboot.example.talentlens.Repositories.RecruiterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final RecruiterRepository recruiterRepository;
    private final CandidateRepository candidateRepository;
    private final EmailService emailService;

    public AdminService(RecruiterRepository recruiterRepository, CandidateRepository candidateRepository, EmailService emailService) {
        this.recruiterRepository = recruiterRepository;
        this.candidateRepository = candidateRepository;
        this.emailService = emailService;
    }

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public Recruiter getRecruiterDetails(Long id) {
        return recruiterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
    }

    public ResponseMessage updateRecruiterStatus(Long id, RecruiterStatusRequest request) {
        Recruiter recruiter = getRecruiterDetails(id);
        String currentStatus = request.getStatus().toUpperCase();

        if (currentStatus.equals("APPROVED")) {
            recruiter.setApplicationStatus(AIExtractionStatus.APPROVED);

            String body = "<html><body>" +
                    "<h2>Congratulations!</h2>" +
                    "<p>Your account with <b>TalentLens</b> has been successfully approved.</p>" +
                    "<p>You can now log into the platform and start posting jobs ad get the right talent i less time.</p>" +
                    "</body></html>";
            emailService.sendEmail(recruiter.getCompanyEmail(), "Account Approved - TalentLens", body);

        } else if (currentStatus.equals("DENIED")) {
            recruiter.setApplicationStatus(AIExtractionStatus.DENIED);

            String body = getString(request);
            emailService.sendEmail(recruiter.getCompanyEmail(), "Account Denied - TalentLens", body);
        } else {
            throw new IllegalArgumentException("Invalid status provided. Use APPROVED or DENIED.");
        }

        recruiterRepository.save(recruiter);

        return new ResponseMessage(HttpStatus.OK ,"Recruiter status updated to " + currentStatus + " and email sent.");
    }

    private static String getString(RecruiterStatusRequest request) {
        String reason = request.getReason() != null && !request.getReason().isBlank()
                ? request.getReason()
                : "Your company details did not meet our platform criteria at this time.";

        String message = "<html><body>" +
                "<h2>Account Status Update</h2>" +
                "<p>We have reviewed your recruiter application for <b>TalentLens</b>.</p>" +
                "<p>Unfortunately, your application has been denied.</p>" +
                "<p><b>Reason:</b> " + reason + "</p>" +
                "</body></html>";
        return message;
    }

    public List<CandidateProfile> getAllCandidates() {
        return candidateRepository.findAll();
    }
}