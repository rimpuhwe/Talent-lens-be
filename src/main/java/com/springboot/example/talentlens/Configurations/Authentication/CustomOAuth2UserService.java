package com.springboot.example.talentlens.Configurations.Authentication;

import com.springboot.example.talentlens.Candidate.CandidateProfile;
import com.springboot.example.talentlens.Enums.Role;
import com.springboot.example.talentlens.Repositories.CandidateRepository;
import com.springboot.example.talentlens.User.User;
import com.springboot.example.talentlens.Repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    public CustomOAuth2UserService(UserRepository userRepository , CandidateRepository candidateRepository) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;

    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String username = (String) attributes.get("email");
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttributeKey;
        String fullName;


        if ("google".equals(registrationId)) {
            nameAttributeKey = "sub";
            fullName = (String) attributes.get("given_name");
        } else if ("github".equals(registrationId)) {
            nameAttributeKey = "id";
            fullName = (String) attributes.get("login");
        } else {
            throw new OAuth2AuthenticationException("Unknown provider: " + registrationId);
        }

        if(userRepository.findByUsername(username).isEmpty()){
            try {
                User user = new User();
                user.setFullName(fullName);
                user.setAuthProvider(registrationId);
                user.setUsername(username);
                user.setPassword("");
                user.setRole(assignRole());
                userRepository.save(user);
                if (user.getRole() == Role.CANDIDATE) {
                    CandidateProfile profile = getCandidateProfile(username, fullName);
                    candidateRepository.save(profile);
                }
            } catch (Exception e) {
                System.out.println("User with email " + username + " already exists or constraint violation occurred: " + e.getMessage());
            }
        }


        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + assignRole())),
                attributes,
                nameAttributeKey
        );
    }

    private static CandidateProfile getCandidateProfile(String username, String fullName) {
        CandidateProfile profile = new CandidateProfile();
        profile.setEmailAddress(username);
        profile.setFirstName(fullName);

        if (fullName != null) {
            String[] nameParts = fullName.split(" ", 2);
            profile.setFirstName(nameParts[0]);
            if (nameParts.length > 1) {
                profile.setLastName(nameParts[1]);
            }
        }
        return profile;
    }

    public Role assignRole() {
            return  Role.CANDIDATE;
    }


}
