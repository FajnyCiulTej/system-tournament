package com.example.system_tournament.controller;

import com.example.system_tournament.dto.TeamRequestDto;
import com.example.system_tournament.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/request")
    public String createTeamRequest(@RequestBody TeamRequestDto dto,
                                    Authentication authentication) {

        String captain = authentication.getName();
        return teamService.createTeamRequest(captain, dto);
    }
}
