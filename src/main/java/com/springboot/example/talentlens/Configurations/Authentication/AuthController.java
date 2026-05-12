package com.springboot.example.talentlens.Configurations.Authentication;

import com.springboot.example.talentlens.DTOs.*;
import com.springboot.example.talentlens.Exceptions.Custom.InvalidRecord;
import java.io.IOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name ="Authentication" ,  description = "Authentication and authorization endpoints for candidates , recruiters and the admin(users of the system)")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register/candidate")
    @Operation(
            summary = "Register candidate",
            description = "Creates a new candidate account"
    )
    public ResponseEntity<ResponseMessage> registerCandidate(@Valid  @RequestBody CandidateRegister candidate) {
        return new ResponseEntity<>(service.registerCandidate(candidate), HttpStatus.CREATED) ;
    }

    @PostMapping("/register/recruiter")
    @Operation(
            summary = "Register recruiter",
            description = "Creates a new recruiter account"
    )
    public ResponseEntity<ResponseMessage> registerRecruiter(@Valid  @RequestBody RecruiterRegister recruiter) {
        return new ResponseEntity<>(service.registerRecruiter(recruiter), HttpStatus.CREATED) ;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticates a user and returns JWT authentication details"
    )
    public ResponseEntity<LoginMessage> login(@RequestParam String username,
                                              @RequestParam String password) throws InvalidRecord {
        return new ResponseEntity<>(service.login(username, password) , HttpStatus.OK);
    }
    @PostMapping("/verify-otp")
    @Operation(
            summary = "Verify OTP",
            description = "Verifies the one-time password sent to the user email"
    )
    public ResponseEntity<ResponseMessage> verifyOtp(@Valid @RequestBody OtpRequest otpRequest){
        return new ResponseEntity<>(service.verifyOtp(otpRequest), HttpStatus.OK);
    }

    @GetMapping("/social-login/{provider}")
    @Operation(
            summary = "Social login redirect",
            description = "Redirects the user to the OAuth2 authentication provider"
    )
    public void redirectToProvider(@PathVariable String provider, HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/" + provider);
    }

    @GetMapping("/user")
    @Operation(
            summary = "Get authenticated OAuth2 user",
            description = "Returns authenticated OAuth2 user attributes"
    )
    public Object user(@AuthenticationPrincipal OAuth2User user) {
        return user.getAttributes();
    }

}