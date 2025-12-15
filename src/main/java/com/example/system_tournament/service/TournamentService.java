package com.example.system_tournament.service;

import com.example.system_tournament.dto.TournamentBracketResponse;

public interface TournamentService {
    void createTournament(String name);
    void registerTeam(Long tournamentId, Long teamId);
    void saveMatchResult(Long matchId, Long winnerId);
    TournamentBracketResponse getBracket(Long id);
}
