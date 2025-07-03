package com.dcvetanovic.release_tracker.dto;

import com.dcvetanovic.release_tracker.enums.Status;

import java.time.LocalDate;

public record ReleaseDTO(
        String name,
        String description,
        Status status,
        LocalDate releaseDate
) {}
