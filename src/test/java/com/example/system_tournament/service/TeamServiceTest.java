package com.example.system_tournament.service;

import com.example.system_tournament.dto.TeamRequestDto;
import com.example.system_tournament.repository.TeamRepository;
import com.example.system_tournament.repository.TeamRequestRepository;
import com.example.system_tournament.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @Test
    void shouldThrowWhenCaptainDoesNotExist() {
        UserRepository userRepository = mock(UserRepository.class);
        TeamRequestRepository requestRepository = mock(TeamRequestRepository.class);
        TeamRepository teamRepository = mock(TeamRepository.class);

        when(userRepository.findByUsername("captain"))
                .thenReturn(Optional.empty());

        TeamServiceImpl service =
                new TeamServiceImpl(userRepository, requestRepository, teamRepository);

        TeamRequestDto dto = new TeamRequestDto();
        dto.setTeamName("TeamX");
        dto.setPlayerIds(List.of(1L, 2L, 3L, 4L));

        assertThrows(RuntimeException.class, () ->
                service.createTeamRequest("captain", dto));
    }
}
