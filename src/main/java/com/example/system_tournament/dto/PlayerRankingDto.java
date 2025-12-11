package com.example.system_tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerRankingDto {

    private Long userId;
    private String username;
    private int kills;
    private int assists;
    private int deaths;
    private double kda;
}
