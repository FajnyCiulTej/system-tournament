package com.example.system_tournament.service;

import com.example.system_tournament.dto.TeamRequestDto;
import com.example.system_tournament.dto.TeamRequestViewDto;
import com.example.system_tournament.model.Team;
import com.example.system_tournament.model.TeamRequest;
import com.example.system_tournament.model.TeamStatus;
import com.example.system_tournament.model.User;
import com.example.system_tournament.repository.TeamRepository;
import com.example.system_tournament.repository.TeamRequestRepository;
import com.example.system_tournament.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRequestRepository teamRequestRepository;
    private final TeamRepository teamRepository;

    public void createTeamRequest(String captainUsername, TeamRequestDto dto) {

        User captain = userRepository.findByUsername(captainUsername)
                .orElseThrow(() -> new RuntimeException("Kapitan nie istnieje."));

        if (teamRequestRepository.existsByCaptainAndStatus(captain, TeamStatus.PENDING)) {
            throw new RuntimeException("Masz już wysłane zgłoszenie drużyny.");
        }

        if (teamRequestRepository.existsByTeamNameAndStatus(dto.getTeamName(), TeamStatus.PENDING)) {
            throw new RuntimeException("Taka nazwa drużyny już istnieje w oczekujących zgłoszeniach.");
        }

        if (dto.getPlayerIds().size() != 4) {
            throw new RuntimeException("Drużyna musi mieć dokładnie 5 osób (kapitan + 4 graczy).");
        }

        List<User> players = userRepository.findAllById(dto.getPlayerIds());

        if (players.size() != 4) {
            throw new RuntimeException("Jeden lub więcej graczy nie istnieje.");
        }

        for (User p : players) {
            if (teamRequestRepository.existsByRequestedPlayersContainingAndStatus(p, TeamStatus.PENDING)) {
                throw new RuntimeException("Jeden z graczy jest już zgłoszony w innej drużynie.");
            }
        }

        TeamRequest request = new TeamRequest();
        request.setTeamName(dto.getTeamName());
        request.setCaptain(captain);
        request.setRequestedPlayers(players);
        request.setStatus(TeamStatus.PENDING);

        teamRequestRepository.save(request);
    }


    public List<TeamRequestViewDto> getPendingRequests() {

        List<TeamRequest> requests = teamRequestRepository.findByStatus(TeamStatus.PENDING);

        return requests.stream().map(req -> {
            TeamRequestViewDto dto = new TeamRequestViewDto();
            dto.setId(req.getId());
            dto.setTeamName(req.getTeamName());
            dto.setCaptainName(req.getCaptain().getUsername());
            dto.setPlayerNames(req.getRequestedPlayers().stream().map(User::getUsername).toList());
            dto.setStatus(req.getStatus().name());
            return dto;
        }).toList();
    }


    public void approveTeamRequest(Long requestId) {

        TeamRequest request = teamRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Zgłoszenie nie istnieje."));

        if (request.getStatus() != TeamStatus.PENDING) {
            throw new RuntimeException("To zgłoszenie nie jest już aktywne.");
        }

        Team team = new Team();
        team.setTeamName(request.getTeamName());
        team.setCaptain(request.getCaptain());
        team.setPlayers(new ArrayList<>(request.getRequestedPlayers()));

        teamRepository.save(team);

        request.setStatus(TeamStatus.APPROVED);
        teamRequestRepository.save(request);
    }


    public void rejectTeamRequest(Long requestId) {

        TeamRequest request = teamRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Zgłoszenie nie istnieje."));

        if (request.getStatus() != TeamStatus.PENDING) {
            throw new RuntimeException("To zgłoszenie nie jest już aktywne.");
        }

        request.setStatus(TeamStatus.REJECTED);
        teamRequestRepository.save(request);
    }
}
