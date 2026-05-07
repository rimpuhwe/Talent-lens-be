package com.springboot.example.talentlens.DTOs;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class CandidateRegister {

    @NotNull(message = "Provide the first name")
    private String firstName;

    @NotBlank(message = "last name can not be blank")
    private String lastName;

    @Email(message = "Provide valid email", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String email;

    @Pattern(message = "password must be strong" , regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    @NotBlank(message = "The password must be provided for security purpose")
    private String password;

    @NotNull
    @PastOrPresent(message = "the date must be of today or past not in future")
    private Date birthDate;
}
