package com.springboot.example.talentlens.DTOs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class JobRequestDto {
    @NotBlank(message = "Job position is required")
    private String jobPosition;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    @NotNull(message = "Required skills cannot be null")
    private List<SkillSet> requiredSkills;

    @NotBlank(message = "Work type is required")
    private String workType;

    private String experienceLevel;
}