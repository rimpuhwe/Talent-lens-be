package com.springboot.example.talentlens.DTOs;

import com.springboot.example.talentlens.Enums.Gender;
import com.springboot.example.talentlens.Enums.JobStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class CandidateProfileDto {


    @Size(max = 3, message = "You can select a maximum of 3 roles")
    private List<String> jobRoles;

    @Enumerated(EnumType.STRING)
    private JobStatus workConditions;

    @NotBlank
    private String professionalProfile;

    @Pattern(regexp = "^(https?://)?(www\\.)?linkedin\\.com/.*$", message = "Must be a valid LinkedIn URL")
    private String linkedInProfile;


    private String firstName;

    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Provide a valid phone number")
    private String phoneNumber;

    private String biography;

    private Date birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
