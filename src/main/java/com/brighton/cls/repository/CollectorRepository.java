package com.brighton.cls.repository;

import com.brighton.cls.domain.Collector;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Collector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectorRepository extends JpaRepository<Collector, Long> {
}
