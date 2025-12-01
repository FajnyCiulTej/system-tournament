package com.example.system_tournament.controller;

import com.example.system_tournament.dto.LoginRequest;
import com.example.system_tournament.dto.RegisterRequest;
import com.example.system_tournament.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        String result = userService.register(request.getUsername(), request.getPassword());

        return switch (result) {
            case "USER_EXISTS" -> "Użytkownik o takiej nazwie już istnieje.";
            case "SUCCESS" -> "Rejestracja zakończona sukcesem.";
            default -> "Błąd rejestracji.";
        };
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        try {
            httpRequest.login(request.getUsername(), request.getPassword());
            return "Zalogowano pomyslnie";
        } catch (ServletException e) {
            return "Błedny login lub haslo";
        }
    }

}
