package com.springboot.example.talentlens.DTOs;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvidenceSubmissionRequest {

    @NotNull(message = "Module ID is required")
    private Long moduleId;

    @NotBlank(message = "Answer cannot be blank")
    @Column(columnDefinition = "TEXT")
    private String answer;
}