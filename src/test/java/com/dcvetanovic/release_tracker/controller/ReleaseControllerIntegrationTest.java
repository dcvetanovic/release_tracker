package com.dcvetanovic.release_tracker.controller;

import com.dcvetanovic.release_tracker.dto.ReleaseDTO;
import com.dcvetanovic.release_tracker.enums.Status;
import com.dcvetanovic.release_tracker.repository.ReleaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReleaseControllerIntegrationTest {

    @Autowired
    ReleaseRepository releaseRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ReleaseDTO testRelease;

    @BeforeEach
    void setup() {
        // Clear H2 between tests
        releaseRepository.deleteAll();

        testRelease = new ReleaseDTO("Test Release", "Integration Test Desc", Status.CREATED, LocalDate.now().plusDays(7));
    }

    @Test
    void shouldCreateNewRelease() throws Exception {
        ReleaseDTO releaseDTO = new ReleaseDTO("Release 1", "Initial release", Status.CREATED, LocalDate.now().plusDays(5));

        mockMvc.perform(post("/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(releaseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Release 1"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void shouldGetAllReleases() throws Exception {

        createMockRelease();

        mockMvc.perform(get("/releases"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Release"));
    }

    @Test
    void shouldGetReleaseById() throws Exception {

        // Create a release
        String response = createMockRelease();

        // Get by id
        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/releases/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Release"));
    }

    @Test
    void shouldFilterReleasesByName() throws Exception {

        // Create two releases with different statuses
        ReleaseDTO release1 = new ReleaseDTO("Release A", "Desc A", Status.CREATED, LocalDate.now().plusDays(1));

        mockMvc.perform(post("/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(release1)))
                .andExpect(status().isCreated());

        // Filter by name
        mockMvc.perform(get("/releases")
                        .param("name", "Release A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Release A"));
    }

    @ParameterizedTest
    @CsvSource({
            "CREATED, Release A, CREATED",
            "ON_PROD, Release B, ON_PROD"
    })
    void shouldFilterReleasesByStatus(String statusParam, String expectedName, String expectedStatus) throws Exception {
        // Create two releases with different statuses
        ReleaseDTO release1 = new ReleaseDTO("Release A", "Desc A", Status.CREATED, LocalDate.now().plusDays(1));
        ReleaseDTO release2 = new ReleaseDTO("Release B", "Desc B", Status.ON_PROD, LocalDate.now().plusDays(2));

        mockMvc.perform(post("/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(release1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(release2)))
                .andExpect(status().isCreated());

        // Filter by status CREATED
        mockMvc.perform(get("/releases").param("status", statusParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(expectedName))
                .andExpect(jsonPath("$[0].status").value(expectedStatus));
    }

    @Test
    void shouldFilterReleasesByReleaseDate() throws Exception {

        // Create two releases with different statuses
        LocalDate releaseDate = LocalDate.now().plusDays(1);
        ReleaseDTO release1 = new ReleaseDTO("Release A", "Desc A", Status.CREATED, releaseDate);

        mockMvc.perform(post("/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(release1)))
                .andExpect(status().isCreated());

        // Filter by name
        mockMvc.perform(get("/releases")
                        .param("releaseDate", releaseDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Release A"));
    }

    @Test
    void shouldReturnNotFoundForMissingRelease() throws Exception {
        mockMvc.perform(get("/releases/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateRelease() throws Exception {
        // First create one
        String response = createMockRelease();

        long id = objectMapper.readTree(response).get("id").asLong();

        ReleaseDTO updated = new ReleaseDTO("Updated Release", "Updated Desc", Status.ON_PROD, LocalDate.now().plusDays(10));

        mockMvc.perform(put("/releases/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Release"))
                .andExpect(jsonPath("$.status").value("ON_PROD"));
    }

    @Test
    void shouldDeleteRelease() throws Exception {
        // First create one
        String response = createMockRelease();

        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/releases/" + id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));


        mockMvc.perform(get("/releases/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForInvalidStatus() throws Exception {
        mockMvc.perform(get("/releases").param("status", "INVALID_STATUS"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundResponseWhenReleaseDoesNotExist() throws Exception {
        mockMvc.perform(get("/releases/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Release not found"))
                .andExpect(jsonPath("$.message").value("Release with id 999999 not found."));
    }

    private String createMockRelease() throws Exception {
        return mockMvc.perform(post("/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRelease)))
                .andReturn().getResponse().getContentAsString();
    }
}
