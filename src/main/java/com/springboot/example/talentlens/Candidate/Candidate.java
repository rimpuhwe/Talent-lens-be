package com.springboot.example.talentlens.Candidate;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(unique = true, length = 255)
    private String email;

    @Column(length = 50)
    private String phone;


    @Column(name = "linkedin_url", length = 500)
    private String linkedInProfile;



}
