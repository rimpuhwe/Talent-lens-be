package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.Recruiter.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByCompanyEmail(String username);
}