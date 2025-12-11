package com.example.system_tournament.controller;

import com.example.system_tournament.dto.PlayerRankingDto;
import com.example.system_tournament.dto.TeamRankingDto;
import com.example.system_tournament.model.PlayerStats;
import com.example.system_tournament.model.Team;
import com.example.system_tournament.repository.PlayerStatsRepository;
import com.example.system_tournament.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final TeamRepository teamRepository;
    private final PlayerStatsRepository playerStatsRepository;

    @GetMapping("/teams")
    public List<TeamRankingDto> getTeamRanking() {
        return teamRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Team::getPoints).reversed())
                .map(team -> new TeamRankingDto(
                        team.getId(),
                        team.getTeamName(),
                        team.getPoints(),
                        team.getCaptain().getUsername()
                ))
                .toList();
    }

    @GetMapping("/players")
    public List<PlayerRankingDto> getPlayerRanking() {
        return playerStatsRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(PlayerStats::getKda).reversed())
                .map(stats -> new PlayerRankingDto(
                        stats.getUser().getId(),
                        stats.getUser().getUsername(),
                        stats.getKills(),
                        stats.getAssists(),
                        stats.getDeaths(),
                        stats.getKda()
                ))
                .toList();
    }
}
