package com.example.system_tournament.service;

import com.example.system_tournament.model.Team;
import com.example.system_tournament.model.User;
import com.example.system_tournament.repository.PlayerStatsRepository;
import com.example.system_tournament.repository.TeamRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RankingServiceTest {

    @Test
    void shouldReturnTeamsSortedByPoints() {
        TeamRepository teamRepository = mock(TeamRepository.class);
        PlayerStatsRepository statsRepository = mock(PlayerStatsRepository.class);

        User captainA = new User();
        captainA.setUsername("capA");

        User captainB = new User();
        captainB.setUsername("capB");

        Team a = new Team();
        a.setTeamName("A");
        a.setPoints(5);
        a.setCaptain(captainA);

        Team b = new Team();
        b.setTeamName("B");
        b.setPoints(10);
        b.setCaptain(captainB);

        when(teamRepository.findAll()).thenReturn(List.of(a, b));

        RankingServiceImpl service =
                new RankingServiceImpl(teamRepository, statsRepository);

        var result = service.getTeamRanking();

        assertEquals("B", result.get(0).getTeamName());
    }
}
