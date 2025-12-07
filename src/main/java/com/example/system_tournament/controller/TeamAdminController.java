package com.example.system_tournament.controller;

import com.example.system_tournament.dto.TeamRequestViewDto;
import com.example.system_tournament.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/team")
@RequiredArgsConstructor
public class TeamAdminController {

    private final TeamService teamService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/requests")
    public List<TeamRequestViewDto> getRequests() {
        return teamService.getPendingRequests();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/requests/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        return teamService.approveTeamRequest(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/requests/reject/{id}")
    public String rejectRequest(@PathVariable Long id) {
        return teamService.rejectTeamRequest(id);
    }
}
