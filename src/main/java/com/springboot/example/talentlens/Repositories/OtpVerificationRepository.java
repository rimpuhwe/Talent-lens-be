package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.User.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    OtpVerification findByEmail(String email);

    Optional<OtpVerification>findByEmailAndOtp(String email, String otp);
}