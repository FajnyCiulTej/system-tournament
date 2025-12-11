package com.example.system_tournament.service;

import com.example.system_tournament.model.Role;
import com.example.system_tournament.model.User;
import com.example.system_tournament.repository.RoleRepository;
import com.example.system_tournament.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(String username, String password) {

        if (userRepository.existsByUsername(username)) {
            return "USER_EXISTS";
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singleton(userRole))
                .build();

        userRepository.save(user);

        return "SUCCESS";
    }

}
