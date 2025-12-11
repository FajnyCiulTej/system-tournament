package com.example.system_tournament.service;

import com.example.system_tournament.dto.TournamentBracketResponse;
import com.example.system_tournament.model.*;
import com.example.system_tournament.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final UserRepository userRepository;

    public void createTournament(String name) {

        Tournament t = new Tournament();
        t.setName(name);
        t.setStatus(TournamentStatus.WAITING);

        tournamentRepository.save(t);
    }


    public void registerTeam(Long tournamentId, Long teamId) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Turniej nie istnieje."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Drużyna nie istnieje."));

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!team.getCaptain().getUsername().equals(loggedUser)) {
            throw new RuntimeException("Tylko kapitan drużyny może zgłosić ją do turnieju.");
        }

        if (tournament.getTeams().contains(team)) {
            throw new RuntimeException("Drużyna jest już zapisana.");
        }

        if (tournament.getTeams().size() >= 8) {
            throw new RuntimeException("Turniej jest już pełny.");
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


    public void saveMatchResult(Long matchId, Long winnerId) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Mecz nie istnieje."));

        if (match.isFinished()) {
            throw new RuntimeException("Ten mecz jest już zakończony.");
        }

        Team teamA = match.getTeamA();
        Team teamB = match.getTeamB();

        if (!teamA.getId().equals(winnerId) && !teamB.getId().equals(winnerId)) {
            throw new RuntimeException("Podany zwycięzca nie gra w tym meczu.");
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

        boolean allFinished =
                roundMatches.stream().allMatch(Match::isFinished);

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


    public TournamentBracketResponse getBracket(Long id) {
        Tournament t = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turniej nie istnieje."));

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

}
