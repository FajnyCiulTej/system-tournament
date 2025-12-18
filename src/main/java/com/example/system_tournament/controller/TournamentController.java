package com.example.system_tournament.controller;

import com.example.system_tournament.dto.TournamentBracketResponse;
import com.example.system_tournament.dto.TournamentCreateRequest;
import com.example.system_tournament.model.Tournament;
import com.example.system_tournament.repository.TournamentRepository;
import com.example.system_tournament.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournament")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final TournamentRepository tournamentRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createTournament(@RequestBody TournamentCreateRequest request) {
        tournamentService.createTournament(request.getName());
        return ResponseEntity.ok("Turniej został utworzony.");
    }

    @PostMapping("/{id}/register/{teamId}")
    public ResponseEntity<String> registerTeam(@PathVariable Long id, @PathVariable Long teamId) {
        tournamentService.registerTeam(id, teamId);
        return ResponseEntity.ok("Drużyna została zapisana.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/match/{matchId}/result")
    public ResponseEntity<String> saveResult(@PathVariable Long matchId, @RequestParam Long winnerId) {
        tournamentService.saveMatchResult(matchId, winnerId);
        return ResponseEntity.ok("Wynik został zapisany.");
    }

    @GetMapping("/{id}/bracket")
    public TournamentBracketResponse getBracket(@PathVariable Long id) {
        return tournamentService.getBracket(id);
    }

    @GetMapping
    public Page<Tournament> getTournaments(
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return tournamentService.getTournaments(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }
}
