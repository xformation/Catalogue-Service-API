package com.brighton.cls.repository;

import com.brighton.cls.domain.ManageView;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ManageView entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManageViewRepository extends JpaRepository<ManageView, Long> {
}
