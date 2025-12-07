package com.example.system_tournament.dto;

import lombok.Data;
import java.util.List;

@Data
public class TeamRequestViewDto {
    private Long id;
    private String teamName;
    private String captainName;
    private List<String> playerNames;
    private String status;
}
