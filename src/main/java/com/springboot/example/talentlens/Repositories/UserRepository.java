package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.User.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String email);
}