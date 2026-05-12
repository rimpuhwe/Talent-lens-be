package com.springboot.example.talentlens.Controllers;

import com.springboot.example.talentlens.Candidate.CandidateProfile;
import com.springboot.example.talentlens.DTOs.CandidateProfileDto;
import com.springboot.example.talentlens.DTOs.ResponseMessage;
import com.springboot.example.talentlens.Services.CandidateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@PreAuthorize("hasRole('CANDIDATE')")
@Tag(name = "Candidate Passport")
public class CandidateProfileController {

    private final CandidateService candidateService;

    public CandidateProfileController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }


    @GetMapping("/me")
    public ResponseEntity<CandidateProfile> getProfile() {
        CandidateProfile profile = candidateService.getMyProfile();
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }


    @PostMapping("/complete")
    public ResponseEntity<CandidateProfile> completeProfile(@Valid @RequestBody CandidateProfileDto request) {
        CandidateProfile updatedProfile = candidateService.updateOrCompleteProfile(request);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<CandidateProfile> updateProfile(@Valid @RequestBody CandidateProfileDto request) {
        CandidateProfile updatedProfile = candidateService.updateOrCompleteProfile(request);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }


    @PostMapping(value = "/upload-cv", consumes = "multipart/form-data")
    public ResponseEntity<ResponseMessage> uploadCv(@RequestParam("file") MultipartFile file) {
        try {
            candidateService.uploadCv(file);
            return new ResponseEntity<>(
                    new ResponseMessage(HttpStatus.OK ,"CV uploaded successfully and profile completion re-evaluated."),
                    HttpStatus.OK
            );
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(
                    new ResponseMessage(HttpStatus.BAD_REQUEST , e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR , "An error occurred while uploading the CV: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getProfileStatus() {
        Map<String, Object> status = candidateService.getProfileStatus();
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
