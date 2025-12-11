package com.example.system_tournament.repository;

import com.example.system_tournament.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTournamentId(Long id);
    List<Match> findByTournamentIdAndRound(Long id, int round);
    List<Match> findByTournamentIdOrderByRoundAsc(Long tournamentId);
}
