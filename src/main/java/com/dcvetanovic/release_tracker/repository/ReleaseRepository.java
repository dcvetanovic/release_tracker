package com.dcvetanovic.release_tracker.repository;


import com.dcvetanovic.release_tracker.entity.Release;
import com.dcvetanovic.release_tracker.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReleaseRepository extends JpaRepository<Release,Long> {
    List<Release> findByStatus(Status status);
    List<Release> findByName(String name);
    List<Release> findByReleaseDate(LocalDate releaseDate);
}
