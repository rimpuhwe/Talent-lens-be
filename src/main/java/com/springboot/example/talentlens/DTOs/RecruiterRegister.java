package com.springboot.example.talentlens.DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterRegister {

    @NotBlank(message = "fill in the company's name")
    private String companyName;

    @Email(message = "Provide valid email", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String emailAddress;

    @Pattern(message = "provide a working phone number" , regexp = "^\\+?[0-9]{10,15}$")
    private String contactNumber;

    @Pattern(message = "password must be strong" , regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    @NotBlank(message = "The password must be provided for security purpose")
    private String password;

    @NotBlank(message = "the location must be valid ")
    private String location;

    @Column(columnDefinition = "TEXT")
    private String companySummary;
}
