package com.example.system_tournament.controller;

import com.example.system_tournament.dto.TeamRequestDto;
import com.example.system_tournament.model.Team;
import com.example.system_tournament.repository.TeamRepository;
import com.example.system_tournament.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamRepository teamRepository;

    @PostMapping("/request")
    public ResponseEntity<String> createTeamRequest(@RequestBody TeamRequestDto dto,
                                                    Authentication authentication) {
        String captain = authentication.getName();
        teamService.createTeamRequest(captain, dto);
        return ResponseEntity.ok("Zgłoszenie zostało wysłane.");
    }

    @GetMapping
    public Page<Team> getTeams(
            @PageableDefault(
                    size = 20,
                    sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return teamService.getTeams(pageable);
    }
}
