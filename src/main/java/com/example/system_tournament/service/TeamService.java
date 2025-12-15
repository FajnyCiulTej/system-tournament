package com.example.system_tournament.service;

import com.example.system_tournament.dto.TeamRequestDto;
import com.example.system_tournament.dto.TeamRequestViewDto;

import java.util.List;

public interface TeamService {
    void createTeamRequest(String captainUsername, TeamRequestDto dto);
    List<TeamRequestViewDto> getPendingRequests();
    void approveTeamRequest(Long requestId);
    void rejectTeamRequest(Long requestId);
}
