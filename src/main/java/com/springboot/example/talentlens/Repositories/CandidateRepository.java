package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.Candidate.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
}