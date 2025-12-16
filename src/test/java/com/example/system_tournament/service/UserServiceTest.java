package com.example.system_tournament.service;

import com.example.system_tournament.model.Role;
import com.example.system_tournament.repository.RoleRepository;
import com.example.system_tournament.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRepository userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);

        Role role = new Role();
        role.setName("ROLE_USER");

        when(userRepository.existsByUsername("test")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(encoder.encode("pass")).thenReturn("hashed");

        UserServiceImpl service = new UserServiceImpl(userRepository, roleRepository, encoder);

        String result = service.register("test", "pass");

        assertEquals("SUCCESS", result);
        verify(userRepository).save(any());
    }
}
