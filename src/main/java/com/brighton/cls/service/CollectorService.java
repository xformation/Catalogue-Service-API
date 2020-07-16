package com.brighton.cls.service;

import com.brighton.cls.service.dto.CollectorDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.brighton.cls.domain.Collector}.
 */
public interface CollectorService {

    /**
     * Save a collector.
     *
     * @param collectorDTO the entity to save.
     * @return the persisted entity.
     */
    CollectorDTO save(CollectorDTO collectorDTO);

    /**
     * Get all the collectors.
     *
     * @return the list of entities.
     */
    List<CollectorDTO> findAll();


    /**
     * Get the "id" collector.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CollectorDTO> findOne(Long id);

    /**
     * Delete the "id" collector.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
