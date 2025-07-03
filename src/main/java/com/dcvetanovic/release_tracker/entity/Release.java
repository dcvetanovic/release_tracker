package com.dcvetanovic.release_tracker.entity;


import com.dcvetanovic.release_tracker.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "releases")
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate releaseDate;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdateAt;

    public Release() {
    }

    public Release(Long id, String name, String description, Status status,
                   LocalDate releaseDate, LocalDateTime createdAt, LocalDateTime lastUpdateAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.lastUpdateAt = lastUpdateAt;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(LocalDateTime lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Release release)) return false;
        return Objects.equals(id, release.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
