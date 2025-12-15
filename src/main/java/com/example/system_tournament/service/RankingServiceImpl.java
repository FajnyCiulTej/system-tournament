package com.example.system_tournament.service;

import com.example.system_tournament.dto.PlayerRankingDto;
import com.example.system_tournament.dto.TeamRankingDto;
import com.example.system_tournament.model.PlayerStats;
import com.example.system_tournament.model.Team;
import com.example.system_tournament.repository.PlayerStatsRepository;
import com.example.system_tournament.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final TeamRepository teamRepository;
    private final PlayerStatsRepository playerStatsRepository;

    @Override
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

    @Override
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
