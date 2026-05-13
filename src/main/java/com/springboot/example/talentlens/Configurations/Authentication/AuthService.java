package com.springboot.example.talentlens.Configurations.Authentication;


import com.springboot.example.talentlens.Candidate.CandidateProfile;
import com.springboot.example.talentlens.DTOs.*;
import com.springboot.example.talentlens.Enums.AIExtractionStatus;
import com.springboot.example.talentlens.Enums.Role;
import com.springboot.example.talentlens.Exceptions.Custom.InvalidRecord;
import com.springboot.example.talentlens.Recruiter.Recruiter;
import com.springboot.example.talentlens.Repositories.CandidateRepository;
import com.springboot.example.talentlens.Repositories.OtpVerificationRepository;
import com.springboot.example.talentlens.Repositories.RecruiterRepository;
import com.springboot.example.talentlens.Repositories.UserRepository;
import com.springboot.example.talentlens.Services.EmailService;
import com.springboot.example.talentlens.User.OtpVerification;
import com.springboot.example.talentlens.User.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {
        private final UserRepository repo;
        private final PasswordEncoder encoder;
        private final JwtService jwtService;
        private final CandidateRepository candidateRepository;
        private final RecruiterRepository recruiterRepository;
        private final EmailService emailService;
        private final OtpVerificationRepository otpVerificationRepository;
//        private final User user;

        public AuthService(UserRepository repo, PasswordEncoder encoder, JwtService jwtService ,
                           CandidateRepository candidateRepository , RecruiterRepository recruiterRepository ,
                           EmailService emailService, OtpVerificationRepository otpVerification) {
            this.repo = repo;
            this.encoder = encoder;
            this.jwtService = jwtService;
            this.candidateRepository = candidateRepository;
            this.recruiterRepository = recruiterRepository;
            this.emailService = emailService;
            this.otpVerificationRepository = otpVerification;

        }

        public ResponseMessage registerCandidate(CandidateRegister candidateRegister) {
            if (repo.existsByUsername(candidateRegister.getEmail())) {
                return new ResponseMessage(HttpStatus.CONFLICT , "Email already registered");

            }
            User user = new User();
            user.setFullName(candidateRegister.getFirstName().trim() + " " + candidateRegister.getLastName().trim());
            user.setUsername(candidateRegister.getEmail());
            user.setPassword(encoder.encode(candidateRegister.getPassword()));
            user.setRole(Role.CANDIDATE);
            user.setAuthProvider("JWT");
            repo.save(user);

            CandidateProfile profile = new CandidateProfile();
            profile.setFirstName(candidateRegister.getFirstName().trim());
            profile.setLastName(candidateRegister.getLastName().trim());
            profile.setEmailAddress(candidateRegister.getEmail());
            profile.setBirthDate(candidateRegister.getBirthDate());
            profile.setGender(candidateRegister.getGender());
            candidateRepository.save(profile);

            String OTP = generateOtp(candidateRegister.getEmail());



            emailService.sendVerificationEmail(user.getUsername(), OTP);

            return new ResponseMessage(HttpStatus.CREATED , "Successfully created an account. An OTP has been sent to your registered email address for account verification.");

        }
        public ResponseMessage registerRecruiter(RecruiterRegister recruiterRegister) {
            User user = new User();
            user.setFullName(recruiterRegister.getCompanyName());
            user.setUsername(recruiterRegister.getEmailAddress());
            user.setPassword(encoder.encode(recruiterRegister.getPassword()));
            user.setRole(Role.RECRUITER);
            user.setAuthProvider("JWT");
            repo.save(user);

            Recruiter recruiter = getRecruiter(recruiterRegister);

            emailService.sendConfirmationEmail(user.getUsername());
            recruiterRepository.save(recruiter);

            return new ResponseMessage(HttpStatus.CREATED ,"Successfully created an account. Check your email address for confirmation");
        }


    public LoginMessage login(String username, String password) throws InvalidRecord {

        User user = repo.findByUsername(username)
                .orElseThrow(() -> new InvalidRecord("Username not found"));

        if (!encoder.matches(password, user.getPassword()))
            throw new InvalidRecord("Invalid credentials");


        if (user.getRole().equals(Role.CANDIDATE)) {
            OtpVerification otp = otpVerificationRepository.findByEmail(user.getUsername());

            if (otp == null || !otp.isVerified()) {
                throw new InvalidRecord("Your account is not active. Please verify your email using the OTP.");
            }
        }
        else if (user.getRole().equals(Role.RECRUITER)) {

            Recruiter recruiter = recruiterRepository.findByCompanyEmail(user.getUsername())
                    .orElseThrow(() -> new InvalidRecord("Recruiter profile not found."));

            if (!AIExtractionStatus.APPROVED.equals(recruiter.getApplicationStatus())) {
                throw new InvalidRecord("Your account is pending , waiting for  admin approval.");
            }
        }

        return new LoginMessage(HttpStatus.OK ,"Successfully Login" , jwtService.generateToken(user.getUsername(), user.getRole().name()), user.getRole());
    }


    private  String generateOtp(String email) {
           Random random = new Random();
            int otp = 100000 + random.nextInt(900000);
            OtpVerification otpVerification = new OtpVerification();
            otpVerification.setEmail(email);
            otpVerification.setOtp(String.valueOf(otp));
            otpVerification.setVerified(false);
            otpVerification.setExpiry(LocalDateTime.now().plusMinutes(10));
            otpVerificationRepository.save(otpVerification);
            return String.valueOf(otp);
        }
        public ResponseMessage verifyOtp(OtpRequest request) {
            OtpVerification otp = otpVerificationRepository.findByEmailAndOtp(request.getEmail(), request.getOtp()).orElseThrow(() -> new IllegalStateException("Invalid OTP or email."));
            if (otp.isVerified()) {
               throw new IllegalStateException("Email already verified.");
            }
            if (otp.getExpiry().isBefore(LocalDateTime.now())) {
               throw new IllegalStateException("OTP expired. Please request a new one.");
            }
           otp.setVerified(true);
           otpVerificationRepository.save(otp);
           return new ResponseMessage(HttpStatus.OK,"Successfully verified. Account Activated");
       }
       private static Recruiter getRecruiter(RecruiterRegister recruiterRegister) {
           Recruiter recruiter = new Recruiter();

           recruiter.setCompanyName(recruiterRegister.getCompanyName());
           recruiter.setCompanyAddress(recruiterRegister.getLocation());
           recruiter.setCompanyDescription(recruiterRegister.getCompanySummary());
           recruiter.setCompanyEmail(recruiterRegister.getEmailAddress());
           recruiter.setCompanyPhone(recruiterRegister.getContactNumber());
           recruiter.setApplicationStatus(AIExtractionStatus.PENDING);
           return recruiter;
      }
}


