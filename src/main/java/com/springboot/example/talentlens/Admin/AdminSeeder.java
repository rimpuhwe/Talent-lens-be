package com.springboot.example.talentlens.Admin;

import com.springboot.example.talentlens.Enums.Role;
import com.springboot.example.talentlens.Repositories.UserRepository;
import com.springboot.example.talentlens.User.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@talentlens.rw";
            if (!userRepository.existsByUsername(adminEmail)) {
                User admin = new User();
                admin.setFullName("System Admin");
                admin.setUsername(adminEmail);
                admin.setPassword(passwordEncoder.encode("SuperAdmin@2024!"));
                admin.setRole(Role.SUPER_ADMIN);
                admin.setAuthProvider("JWT");

                userRepository.save(admin);
                System.out.println("Default Super Admin seeded successfully.");
            }
        };
    }
}