package com.example.system_tournament.repository;

import com.example.system_tournament.model.Team;
import com.example.system_tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByTeamName(String teamName);

    boolean existsByCaptain(User captain);

    boolean existsByPlayersContaining(User player);
}
