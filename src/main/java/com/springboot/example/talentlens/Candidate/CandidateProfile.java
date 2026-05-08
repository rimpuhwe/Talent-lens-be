package com.springboot.example.talentlens.Candidate;


import com.springboot.example.talentlens.Enums.Gender;
import com.springboot.example.talentlens.Enums.JobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(unique = true, length = 255)
    private String emailAddress;

    @Column(length = 50)
    private String phoneNumber;


    @Column(name = "linkedin_url", length = 500)
    private String linkedInProfile;

    @NotNull
    private String professionalProfile;

    @NotNull
    @PastOrPresent(message = "the date must be of today or past not in future")
    private Date birthDate;

    private String Biography;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "BYTEA")
    private byte[] cvData;

    private String cvFileName;

    private String cvFileType;

    @ElementCollection
    @CollectionTable(name = "candidate_job_roles", joinColumns = @JoinColumn(name = "candidate_id"))
    private List<String> jobRoles;

    @Enumerated(EnumType.STRING)
    private JobStatus workConditions;


    private boolean profileCompleted = false;

    private int completionPercentage = 0;



}
