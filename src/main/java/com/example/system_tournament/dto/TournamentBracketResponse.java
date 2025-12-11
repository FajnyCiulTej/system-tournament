package com.example.system_tournament.dto;

import com.example.system_tournament.model.Match;
import com.example.system_tournament.model.Tournament;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TournamentBracketResponse {
    private Long tournamentId;
    private String name;
    private int currentRound;
    private List<MatchDto> matches;
    String status;

    public static TournamentBracketResponse from(Tournament t, List<Match> matches) {
        return new TournamentBracketResponse(
                t.getId(),
                t.getName(),
                t.getCurrentRound(),
                matches.stream().map(MatchDto::from).toList(),
                t.getStatus().name()
        );
    }
}

