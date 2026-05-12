package com.springboot.example.talentlens.Services;



import com.springboot.example.talentlens.Candidate.CandidateProfile;
import com.springboot.example.talentlens.DTOs.CandidateProfileDto;
import com.springboot.example.talentlens.Repositories.CandidateRepository;
import com.springboot.example.talentlens.Repositories.UserRepository;
import com.springboot.example.talentlens.User.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;


    public CandidateService(CandidateRepository candidateRepository, UserRepository userRepository , Cloudinary cloudinary) {
        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }


    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public CandidateProfile getMyProfile() {
        String email = getAuthenticatedUser().getUsername();
        return candidateRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Candidate profile missing for this user. Please contact support."));
    }


    public CandidateProfile updateOrCompleteProfile(CandidateProfileDto request) {
        CandidateProfile profile = getMyProfile();


        if (request.getJobRoles() != null) profile.setJobRoles(request.getJobRoles());
        if (request.getWorkConditions() != null) profile.setWorkConditions(request.getWorkConditions());
        if (request.getProfessionalProfile() != null) profile.setProfessionalProfile(request.getProfessionalProfile());
        if (request.getLinkedInProfile() != null) profile.setLinkedInProfile(request.getLinkedInProfile());
        if (request.getPhoneNumber() != null) profile.setPhoneNumber(request.getPhoneNumber());
        if (request.getBiography() != null) profile.setBiography(request.getBiography());
        if (request.getGender() != null) profile.setGender(request.getGender());


        evaluateProfileCompletion(profile);

        return candidateRepository.save(profile);
    }


    public void uploadCv(MultipartFile file) throws IOException {
        CandidateProfile profile = getMyProfile();


        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("application/pdf") &&
                !contentType.equals("application/msword") &&
                !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new IllegalArgumentException("Invalid file type. Only PDF, DOC, and DOCX files are allowed.");
        }


        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "talentlens/candidates/cvs" // Keeps your Cloudinary dashboard organized
        ));


        String fileUrl = uploadResult.get("secure_url").toString();


        profile.setCvUrl(fileUrl);

        evaluateProfileCompletion(profile);
        candidateRepository.save(profile);
    }


    public Map<String, Object> getProfileStatus() {
        CandidateProfile profile = getMyProfile();
        Map<String, Object> status = new HashMap<>();

        status.put("profileCompleted", profile.isProfileCompleted());
        status.put("completionPercentage", profile.getCompletionPercentage());

        return status;
    }


    private void evaluateProfileCompletion(CandidateProfile profile) {

        int totalFields = 5;
        int completedFields = 0;

        if (profile.getJobRoles() != null && !profile.getJobRoles().isEmpty()) completedFields++;
        if (profile.getWorkConditions() != null) completedFields++;
        if (profile.getProfessionalProfile() != null && !profile.getProfessionalProfile().isBlank()) completedFields++;
        if (profile.getLinkedInProfile() != null && !profile.getLinkedInProfile().isBlank()) completedFields++;


        if (profile.getCvUrl() != null && !profile.getCvUrl().isBlank()) completedFields++;


        int percentage = (int) (((double) completedFields / totalFields) * 100);

        profile.setCompletionPercentage(percentage);
        profile.setProfileCompleted(percentage == 100);
    }
}
