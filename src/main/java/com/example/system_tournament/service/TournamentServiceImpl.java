package com.example.system_tournament.service;

import com.example.system_tournament.dto.TournamentBracketResponse;
import com.example.system_tournament.exception.BadRequestException;
import com.example.system_tournament.exception.NotFoundException;
import com.example.system_tournament.model.*;
import com.example.system_tournament.repository.MatchRepository;
import com.example.system_tournament.repository.PlayerStatsRepository;
import com.example.system_tournament.repository.TeamRepository;
import com.example.system_tournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final PlayerStatsRepository playerStatsRepository;

    @Override
    public void createTournament(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Nazwa turnieju nie może być pusta.");
        }
        Tournament t = new Tournament();
        t.setName(name);
        t.setStatus(TournamentStatus.WAITING);
        tournamentRepository.save(t);
    }

    @Override
    @Transactional
    public void registerTeam(Long tournamentId, Long teamId) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new NotFoundException("Turniej nie istnieje."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Drużyna nie istnieje."));

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();

        if (team.getCaptain() == null || !team.getCaptain().getUsername().equals(loggedUser)) {
            throw new BadRequestException("Tylko kapitan drużyny może zgłosić ją do turnieju.");
        }

        if (tournament.getTeams().contains(team)) {
            throw new BadRequestException("Drużyna jest już zapisana.");
        }

        if (tournament.getTeams().size() >= 8) {
            throw new BadRequestException("Turniej jest już pełny.");
        }

        tournament.getTeams().add(team);
        tournamentRepository.save(tournament);

        if (tournament.getTeams().size() == 8) {
            startTournament(tournament);
        }
    }

    private void startTournament(Tournament t) {
        t.setStatus(TournamentStatus.ACTIVE);
        t.setCurrentRound(1);
        tournamentRepository.save(t);

        createMatchesForRound(t, t.getTeams());
    }

    private void createMatchesForRound(Tournament t, List<Team> teams) {
        List<Team> shuffled = new java.util.ArrayList<>(teams);
        Collections.shuffle(shuffled, new Random(System.nanoTime()));

        for (int i = 0; i < shuffled.size(); i += 2) {
            Match match = new Match();
            match.setTournament(t);
            match.setTeamA(shuffled.get(i));
            match.setTeamB(shuffled.get(i + 1));
            match.setRound(t.getCurrentRound());
            match.setFinished(false);
            matchRepository.save(match);
        }
    }

    @Override
    @Transactional
    public void saveMatchResult(Long matchId, Long winnerId) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Mecz nie istnieje."));

        if (match.isFinished()) {
            throw new BadRequestException("Ten mecz jest już zakończony.");
        }

        Team teamA = match.getTeamA();
        Team teamB = match.getTeamB();

        if (teamA == null || teamB == null) {
            throw new BadRequestException("Mecz ma niekompletne drużyny.");
        }

        if (!teamA.getId().equals(winnerId) && !teamB.getId().equals(winnerId)) {
            throw new BadRequestException("Podany zwycięzca nie gra w tym meczu.");
        }

        Team winner = winnerId.equals(teamA.getId()) ? teamA : teamB;
        Team loser = winnerId.equals(teamA.getId()) ? teamB : teamA;

        match.setWinner(winner);
        match.setFinished(true);
        matchRepository.save(match);

        awardPoints(winner, loser);
        updatePlayerStats(teamA);
        updatePlayerStats(teamB);

        finishRoundIfNeeded(match.getTournament());
    }

    private void finishRoundIfNeeded(Tournament t) {

        List<Match> roundMatches =
                matchRepository.findByTournamentIdAndRound(t.getId(), t.getCurrentRound());

        boolean allFinished = roundMatches.stream().allMatch(Match::isFinished);
        if (!allFinished) return;

        List<Team> winners = roundMatches.stream()
                .map(Match::getWinner)
                .toList();

        if (winners.size() == 1) {
            t.setStatus(TournamentStatus.FINISHED);
            awardWinnerBonus(winners.get(0));
            tournamentRepository.save(t);
            return;
        }

        t.setCurrentRound(t.getCurrentRound() + 1);
        tournamentRepository.save(t);

        createMatchesForRound(t, winners);
    }

    private void awardPoints(Team winner, Team loser) {
        winner.setPoints(winner.getPoints() + 1);
        loser.setPoints(loser.getPoints() - 1);
        teamRepository.save(winner);
        teamRepository.save(loser);
    }

    private void awardWinnerBonus(Team champion) {
        champion.setPoints(champion.getPoints() + 5);
        teamRepository.save(champion);
    }

    @Override
    public TournamentBracketResponse getBracket(Long id) {

        Tournament t = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Turniej nie istnieje."));

        List<Match> matches = matchRepository.findByTournamentIdOrderByRoundAsc(id);
        return TournamentBracketResponse.from(t, matches);
    }

    private void updatePlayerStats(Team team) {
        for (User player : team.getPlayers()) {

            PlayerStats stats = playerStatsRepository.findByUser(player)
                    .orElseGet(() -> {
                        PlayerStats s = new PlayerStats();
                        s.setUser(player);
                        return s;
                    });

            int kills = (int) (Math.random() * 20) + 1;
            int assists = (int) (Math.random() * 20) + 1;
            int deaths = (int) (Math.random() * 20) + 1;

            stats.setKills(stats.getKills() + kills);
            stats.setAssists(stats.getAssists() + assists);
            stats.setDeaths(stats.getDeaths() + deaths);

            playerStatsRepository.save(stats);
        }
    }

    @Override
    public Page<Tournament> getTournaments(Pageable pageable) {
        return tournamentRepository.findAll(pageable);
    }

    @Override
    public Tournament getTournamentById(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Turniej nie istnieje"));
    }
}
