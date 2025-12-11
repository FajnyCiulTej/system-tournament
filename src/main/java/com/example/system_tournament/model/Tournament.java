package com.example.system_tournament.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int currentRound = 1;

    @Enumerated(EnumType.STRING)
    private TournamentStatus status = TournamentStatus.WAITING;

    @ManyToMany
    private List<Team> teams = new ArrayList<>();
}
