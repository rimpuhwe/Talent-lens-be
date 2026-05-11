package com.springboot.example.talentlens.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@NoArgsConstructor // REQUIRED for Jackson
@AllArgsConstructor
public class JobGenerationResponseDto {

    @JsonProperty("job_description")
    private String jobDescription;

    @JsonProperty("required_skills")
    private List<String> requiredSkills;
}