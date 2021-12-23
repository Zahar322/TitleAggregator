package com.title.aggregator.jpa.repository;

import com.title.aggregator.jpa.models.Titles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitlesRepository extends JpaRepository<Titles, Long> {

    Titles findFirstByIdNotNull();
}
