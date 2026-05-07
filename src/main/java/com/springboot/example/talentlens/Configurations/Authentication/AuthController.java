package com.springboot.example.talentlens.Configurations.Authentication;

import com.springboot.example.talentlens.DTOs.*;
import com.springboot.example.talentlens.Exceptions.Custom.InvalidRecord;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register/candidate")
    public ResponseEntity<ResponseMessage> registerCandidate(@Valid  @RequestBody CandidateRegister candidate) {
        return new ResponseEntity<>(service.registerCandidate(candidate), HttpStatus.CREATED) ;
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<ResponseMessage> registerRecruiter(@Valid  @RequestBody RecruiterRegister recruiter) {
        return new ResponseEntity<>(service.registerRecruiter(recruiter), HttpStatus.CREATED) ;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginMessage> login(@RequestParam String username,
                                              @RequestParam String password) throws InvalidRecord {
        return new ResponseEntity<>(service.login(username, password) , HttpStatus.OK);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseMessage> verifyOtp(@Valid @RequestBody OtpRequest otpRequest){
        return new ResponseEntity<>(service.verifyOtp(otpRequest), HttpStatus.OK);
    }

    @GetMapping("/social-login/{provider}")
    public void redirectToProvider(@PathVariable String provider, HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/" + provider);
    }

    @GetMapping("/user")
    public Object user(@AuthenticationPrincipal OAuth2User user) {
        return user.getAttributes();
    }

}