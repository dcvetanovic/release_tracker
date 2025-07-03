package com.dcvetanovic.release_tracker.controller;


import com.dcvetanovic.release_tracker.dto.ReleaseDTO;
import com.dcvetanovic.release_tracker.entity.Release;
import com.dcvetanovic.release_tracker.enums.Status;
import com.dcvetanovic.release_tracker.service.ReleaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/releases")
public class ReleaseController {

    private final ReleaseService releaseService;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @GetMapping
    public List<Release> getAllReleases(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate releaseDate
    ) {

        if (name != null) {
            return releaseService.getReleaseByName(name);
        }
        if (status != null) {
            return releaseService.getReleasesByStatus(Status.valueOf(status));
        }
        if (releaseDate != null) {
            return releaseService.getReleaseByReleaseDate(releaseDate);
        }
        return releaseService.getAllReleases();
    }

    @GetMapping("/{id}")
    public Release getRelease(@PathVariable Long id) {
        return releaseService.getRelease(id);
    }

    @PostMapping
    public ResponseEntity<Release> createRelease(@RequestBody Release release) {
        return new ResponseEntity<>(releaseService.createRelease(release), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Release updateRelease(@PathVariable Long id, @RequestBody ReleaseDTO updatedRelease) {
        return releaseService.updateRelease(id, updatedRelease);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        releaseService.deleteRelease(id);
        return ResponseEntity.noContent().build();
    }

}
