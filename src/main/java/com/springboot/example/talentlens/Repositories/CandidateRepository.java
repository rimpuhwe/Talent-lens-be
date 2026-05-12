package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.Candidate.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<CandidateProfile, UUID> {
    Optional<CandidateProfile> findByEmailAddress(String email);
}