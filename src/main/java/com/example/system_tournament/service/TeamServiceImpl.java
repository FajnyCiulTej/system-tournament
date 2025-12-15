package com.example.system_tournament.service;

import com.example.system_tournament.dto.TeamRequestDto;
import com.example.system_tournament.dto.TeamRequestViewDto;
import com.example.system_tournament.exception.BadRequestException;
import com.example.system_tournament.exception.NotFoundException;
import com.example.system_tournament.model.Team;
import com.example.system_tournament.model.TeamRequest;
import com.example.system_tournament.model.TeamStatus;
import com.example.system_tournament.model.User;
import com.example.system_tournament.repository.TeamRepository;
import com.example.system_tournament.repository.TeamRequestRepository;
import com.example.system_tournament.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final UserRepository userRepository;
    private final TeamRequestRepository teamRequestRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public void createTeamRequest(String captainUsername, TeamRequestDto dto) {

        if (dto == null) throw new BadRequestException("Brak danych zgłoszenia.");

        User captain = userRepository.findByUsername(captainUsername)
                .orElseThrow(() -> new NotFoundException("Kapitan nie istnieje."));

        if (dto.getTeamName() == null || dto.getTeamName().isBlank()) {
            throw new BadRequestException("Nazwa drużyny nie może być pusta.");
        }

        if (dto.getPlayerIds() == null || dto.getPlayerIds().size() != 4) {
            throw new BadRequestException("Drużyna musi mieć dokładnie 5 osób (kapitan + 4 graczy).");
        }

        if (teamRequestRepository.existsByCaptainAndStatus(captain, TeamStatus.PENDING)) {
            throw new BadRequestException("Masz już wysłane zgłoszenie drużyny.");
        }

        if (teamRequestRepository.existsByTeamNameAndStatus(dto.getTeamName(), TeamStatus.PENDING)) {
            throw new BadRequestException("Taka nazwa drużyny już istnieje w oczekujących zgłoszeniach.");
        }

        List<User> players = userRepository.findAllById(dto.getPlayerIds());
        if (players.size() != 4) {
            throw new BadRequestException("Jeden lub więcej graczy nie istnieje.");
        }

        for (User p : players) {
            if (teamRequestRepository.existsByRequestedPlayersContainingAndStatus(p, TeamStatus.PENDING)) {
                throw new BadRequestException("Jeden z graczy jest już zgłoszony w innej drużynie.");
            }
        }

        TeamRequest request = new TeamRequest();
        request.setTeamName(dto.getTeamName());
        request.setCaptain(captain);
        request.setRequestedPlayers(players);
        request.setStatus(TeamStatus.PENDING);

        teamRequestRepository.save(request);
    }

    @Override
    public List<TeamRequestViewDto> getPendingRequests() {
        return teamRequestRepository.findByStatus(TeamStatus.PENDING).stream()
                .map(req -> {
                    TeamRequestViewDto dto = new TeamRequestViewDto();
                    dto.setId(req.getId());
                    dto.setTeamName(req.getTeamName());
                    dto.setCaptainName(req.getCaptain().getUsername());
                    dto.setPlayerNames(req.getRequestedPlayers().stream().map(User::getUsername).toList());
                    dto.setStatus(req.getStatus().name());
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional
    public void approveTeamRequest(Long requestId) {

        TeamRequest request = teamRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Zgłoszenie nie istnieje."));

        if (request.getStatus() != TeamStatus.PENDING) {
            throw new BadRequestException("To zgłoszenie nie jest już aktywne.");
        }

        Team team = new Team();
        team.setTeamName(request.getTeamName());
        team.setCaptain(request.getCaptain());
        team.setPlayers(new ArrayList<>(request.getRequestedPlayers()));
        teamRepository.save(team);

        request.setStatus(TeamStatus.APPROVED);
        teamRequestRepository.save(request);
    }

    @Override
    @Transactional
    public void rejectTeamRequest(Long requestId) {

        TeamRequest request = teamRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Zgłoszenie nie istnieje."));

        if (request.getStatus() != TeamStatus.PENDING) {
            throw new BadRequestException("To zgłoszenie nie jest już aktywne.");
        }

        request.setStatus(TeamStatus.REJECTED);
        teamRequestRepository.save(request);
    }
}
