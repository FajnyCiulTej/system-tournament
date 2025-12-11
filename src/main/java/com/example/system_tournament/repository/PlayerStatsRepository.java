package com.example.system_tournament.repository;

import com.example.system_tournament.model.PlayerStats;
import com.example.system_tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {
    Optional<PlayerStats> findByUser(User user);
}
