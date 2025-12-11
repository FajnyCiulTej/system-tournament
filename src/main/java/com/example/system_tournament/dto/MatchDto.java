package com.example.system_tournament.dto;

import com.example.system_tournament.model.Match;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchDto {
    private Long matchId;
    private int round;
    private Long teamAId;
    private String teamA;
    private Long teamBId;
    private String teamB;
    private String winner;
    private boolean finished;

    public static MatchDto from(Match m) {
        return new MatchDto(
                m.getId(),
                m.getRound(),
                m.getTeamA().getId(),
                m.getTeamA().getTeamName(),
                m.getTeamB().getId(),
                m.getTeamB().getTeamName(),
                m.getWinner() != null ? m.getWinner().getTeamName() : null,
                m.isFinished()
        );
    }
}

