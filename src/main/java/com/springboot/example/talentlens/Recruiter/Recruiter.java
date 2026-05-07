package com.springboot.example.talentlens.Recruiter;

import com.springboot.example.talentlens.Enums.AIExtractionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotBlank(message = "fill in the company's name")
    private String CompanyName;


    @NotBlank(message = "the location must be valid ")
    private String CompanyAddress;

    @Pattern(message = "provide a working phone number" , regexp = "^\\+?[0-9]{10,15}$")
    private String CompanyPhone;

    @Column(columnDefinition = "TEXT")
    private String CompanyDescription;

    @Email(message = "Provide valid email", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String companyEmail;

    @Enumerated(EnumType.STRING)
    private AIExtractionStatus ApplicationStatus ;


}
