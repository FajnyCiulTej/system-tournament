package com.example.system_tournament.repository;

import com.example.system_tournament.model.TeamRequest;
import com.example.system_tournament.model.TeamStatus;
import com.example.system_tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {

    boolean existsByTeamNameAndStatus(String teamName, TeamStatus status);

    boolean existsByCaptainAndStatus(User captain, TeamStatus status);

    boolean existsByRequestedPlayersContainingAndStatus(User player, TeamStatus status);

    List<TeamRequest> findByCaptainAndStatus(User captain, TeamStatus status);

    List<TeamRequest> findByStatus(TeamStatus status);

}
