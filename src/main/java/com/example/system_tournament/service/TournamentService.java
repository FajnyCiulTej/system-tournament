package com.example.system_tournament.service;

import com.example.system_tournament.dto.TournamentBracketResponse;
import com.example.system_tournament.model.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TournamentService {
    Page<Tournament> getTournaments(Pageable pageable);
    void createTournament(String name);
    void registerTeam(Long tournamentId, Long teamId);
    void saveMatchResult(Long matchId, Long winnerId);
    TournamentBracketResponse getBracket(Long id);

    Tournament getTournamentById(Long id);
}
