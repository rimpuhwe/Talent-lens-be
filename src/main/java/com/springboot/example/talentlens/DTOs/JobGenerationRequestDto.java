package com.springboot.example.talentlens.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobGenerationRequestDto {
    @JsonProperty("job_position")
    private String jobPosition;

    @JsonProperty("industry_field")
    private String industryField;

    @JsonProperty("company_about")
    private String companyAbout;

    @JsonProperty("additional_instructions")
    private String additionalInstructions;
}