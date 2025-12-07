package com.example.system_tournament.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String teamName;

    @ManyToOne
    private User captain;

    @ManyToMany
    @JoinTable(name = "team_players")
    private List<User> players;
}
