package com.dcvetanovic.release_tracker.service;

import com.dcvetanovic.release_tracker.dto.ReleaseDTO;
import com.dcvetanovic.release_tracker.entity.Release;
import com.dcvetanovic.release_tracker.enums.Status;
import com.dcvetanovic.release_tracker.exception.ReleaseNotFoundException;
import com.dcvetanovic.release_tracker.repository.ReleaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReleaseServiceTest {

    @Mock
    private ReleaseRepository releaseRepository;

    @InjectMocks
    private ReleaseService releaseService;

    private Release release;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        release = new Release(
                1L,
                "Release 1",
                "Description",
                Status.CREATED,
                LocalDate.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void createRelease_ShouldReturnSavedRelease() {
        when(releaseRepository.save(any(Release.class))).thenReturn(release);

        Release result = releaseService.createRelease(release);

        assertNotNull(result);
        assertEquals("Release 1", result.getName());
        verify(releaseRepository).save(any(Release.class));
    }

    @Test
    void getReleaseById_ShouldReturnRelease() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));

        Release result = releaseService.getRelease(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getReleaseById_ShouldThrowWhenNotFound() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ReleaseNotFoundException.class, () -> releaseService.getRelease(1L));
    }

    @Test
    void updateRelease_ShouldUpdateAndReturnRelease() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
        when(releaseRepository.save(any(Release.class))).thenReturn(release);

        ReleaseDTO dto = new ReleaseDTO("Updated", "Updated desc", Status.ON_PROD, LocalDate.now());
        Release result = releaseService.updateRelease(1L, dto);

        assertNotNull(result);
        assertEquals("Updated", result.getName());
        assertEquals(Status.ON_PROD, result.getStatus());
        verify(releaseRepository).save(any(Release.class));
    }

    @Test
    void updateRelease_ShouldThrowWhenNotFound() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.empty());

        ReleaseDTO dto = new ReleaseDTO("Updated", "Updated desc", Status.ON_PROD, LocalDate.now());

        assertThrows(ReleaseNotFoundException.class, () -> releaseService.updateRelease(1L, dto));
    }

    @Test
    void deleteRelease_ShouldDeleteRelease() {
        when(releaseRepository.existsById(1L)).thenReturn(true);

        releaseService.deleteRelease(1L);

        verify(releaseRepository).deleteById(1L);
    }

    @Test
    void deleteRelease_ShouldThrowWhenNotFound() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ReleaseNotFoundException.class, () -> releaseService.deleteRelease(1L));
    }

    @Test
    void filterByName_ShouldReturnMatchingReleases() {
        when(releaseRepository.findByName(anyString()))
                .thenReturn(Collections.singletonList(release));


        List<Release> results = releaseService.getReleaseByName("Release 1");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("Release 1", results.getFirst().getName());
    }

    @Test
    void filterByStatus_ShouldReturnMatchingReleases() {
        when(releaseRepository.findByStatus(Status.CREATED))
                .thenReturn(Collections.singletonList(release));

        List<Release> results = releaseService.getReleasesByStatus(Status.CREATED);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(Status.CREATED, results.getFirst().getStatus());
        verify(releaseRepository).findByStatus(Status.CREATED);
    }

    @Test
    void filterByReleaseDate_ShouldReturnMatchingReleases() {
        LocalDate date = LocalDate.now();
        when(releaseRepository.findByReleaseDate(date))
                .thenReturn(Collections.singletonList(release));

        List<Release> results = releaseService.getReleaseByReleaseDate(date);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(date, results.getFirst().getReleaseDate());
        verify(releaseRepository).findByReleaseDate(date);
    }
}
