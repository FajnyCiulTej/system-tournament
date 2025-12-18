package com.example.system_tournament.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TournamentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void shouldGetTournaments() throws Exception {
        mockMvc.perform(get("/tournament"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenRegisteringToNonExistingTournament() throws Exception {
        mockMvc.perform(
                        post("/tournament/1/register/1")
                )
                .andExpect(status().isNotFound());
    }
}
