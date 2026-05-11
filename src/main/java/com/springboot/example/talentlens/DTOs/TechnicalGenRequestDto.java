package com.springboot.example.talentlens.DTOs;



import lombok.Data;

@Data
public class TechnicalGenRequestDto {
    private String role;
    private String industry;
    private String job_description;
    private String recruiter_focus_areas; // E.g., "Concurrency and Memory Leaks"
}
