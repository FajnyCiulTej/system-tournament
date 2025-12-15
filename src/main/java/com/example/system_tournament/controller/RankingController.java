package com.example.system_tournament.controller;

import com.example.system_tournament.dto.PlayerRankingDto;
import com.example.system_tournament.dto.TeamRankingDto;
import com.example.system_tournament.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/teams")
    public List<TeamRankingDto> getTeamRanking() {
        return rankingService.getTeamRanking();
    }

    @GetMapping("/players")
    public List<PlayerRankingDto> getPlayerRanking() {
        return rankingService.getPlayerRanking();
    }
}
