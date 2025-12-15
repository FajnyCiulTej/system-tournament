package com.example.system_tournament.service;

import com.example.system_tournament.exception.BadRequestException;
import com.example.system_tournament.exception.NotFoundException;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String register(String username, String password) {

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BadRequestException("Login i hasło nie mogą być puste.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Użytkownik o takiej nazwie już istnieje.");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Brak roli ROLE_USER w bazie."));

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singleton(userRole))
                .build();

        userRepository.save(user);
        return "SUCCESS";
    }
}
