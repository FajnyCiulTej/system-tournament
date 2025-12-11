package com.example.system_tournament.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private int kills;
    private int assists;
    private int deaths;

    public double getKda() {
        return (kills + assists) / (double) Math.max(1, deaths);
    }
}
