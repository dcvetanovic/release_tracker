package com.dcvetanovic.release_tracker.service;

import com.dcvetanovic.release_tracker.dto.ReleaseDTO;
import com.dcvetanovic.release_tracker.entity.Release;
import com.dcvetanovic.release_tracker.enums.Status;
import com.dcvetanovic.release_tracker.exception.ReleaseNotFoundException;
import com.dcvetanovic.release_tracker.repository.ReleaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReleaseService {

    private final ReleaseRepository releaseRepository;

    public ReleaseService(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    public List<Release> getAllReleases() {
        return releaseRepository.findAll();
    }

    public Release getRelease(Long id) {
        return releaseRepository.findById(id)
                .orElseThrow(() -> new ReleaseNotFoundException(id));
    }

    public List<Release> getReleasesByStatus(Status status) {
        return releaseRepository.findByStatus(status);
    }

    public List<Release> getReleaseByName(String name) {
        return releaseRepository.findByName(name);
    }

    public List<Release> getReleaseByReleaseDate(LocalDate releaseDate) {
        return releaseRepository.findByReleaseDate(releaseDate);
    }


    public Release createRelease(Release release) {
        release.setCreatedAt(LocalDateTime.now());
        release.setLastUpdateAt(LocalDateTime.now());
        return releaseRepository.save(release);
    }

    public Release updateRelease(Long id, ReleaseDTO updatedRelease) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ReleaseNotFoundException(id));

        release.setName(updatedRelease.name());
        release.setDescription(updatedRelease.description());
        release.setStatus(updatedRelease.status());
        release.setReleaseDate(updatedRelease.releaseDate());
        release.setLastUpdateAt(LocalDateTime.now());

        return releaseRepository.save(release);
    }

    public void deleteRelease(Long id) {
        if (!releaseRepository.existsById(id)) {
            throw new ReleaseNotFoundException(id);
        }
        releaseRepository.deleteById(id);
    }


}
