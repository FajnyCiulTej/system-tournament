package com.example.system_tournament.service;

import com.example.system_tournament.dto.PlayerRankingDto;
import com.example.system_tournament.dto.TeamRankingDto;

import java.util.List;

public interface RankingService {
    List<TeamRankingDto> getTeamRanking();
    List<PlayerRankingDto> getPlayerRanking();
}
