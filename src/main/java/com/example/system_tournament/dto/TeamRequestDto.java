package com.example.system_tournament.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamRequestDto {
    private String teamName;
    private List<Long> playerIds;
}
