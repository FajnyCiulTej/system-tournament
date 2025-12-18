package com.example.system_tournament.service;

import com.example.system_tournament.dto.TeamRequestDto;
import com.example.system_tournament.dto.TeamRequestViewDto;
import com.example.system_tournament.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamService {
    Page<Team> getTeams(Pageable pageable);
    void createTeamRequest(String captainUsername, TeamRequestDto dto);
    List<TeamRequestViewDto> getPendingRequests();
    void approveTeamRequest(Long requestId);
    void rejectTeamRequest(Long requestId);
}
