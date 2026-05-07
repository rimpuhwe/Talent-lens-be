package com.springboot.example.talentlens.User;

import com.springboot.example.talentlens.Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "This field is mandatory")
    @Column(nullable = false)
    private String FullName;

    @Email(message = "Provide valid email", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String username;


    @NotBlank(message = "The password must be provided for security purpose")
    private String Password;

    @Enumerated(EnumType.STRING)
    private Role Role;

    @NotBlank
    private String AuthProvider;

    @CreationTimestamp
    private Timestamp Created_At;

}
