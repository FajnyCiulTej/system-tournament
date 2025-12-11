package com.example.system_tournament.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tournament tournament;

    @ManyToOne
    private Team teamA;

    @ManyToOne
    private Team teamB;

    @ManyToOne
    private Team winner;

    private int round;

    private boolean finished = false;
}
