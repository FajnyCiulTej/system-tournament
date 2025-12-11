package com.example.system_tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamRankingDto {
    private Long id;
    private String teamName;
    private int points;
    private String captainName;
}
