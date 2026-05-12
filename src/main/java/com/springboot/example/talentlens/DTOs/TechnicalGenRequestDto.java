package com.springboot.example.talentlens.DTOs;



import jakarta.persistence.Column;
import lombok.Data;

@Data
public class TechnicalGenRequestDto {
    private String role;
    private String industry;
    @Column(columnDefinition = "TEXT")
    private String job_description;
    private String recruiter_focus_areas; // E.g., "Concurrency and Memory Leaks"
}
