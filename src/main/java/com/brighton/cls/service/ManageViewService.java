package com.brighton.cls.service;

import com.brighton.cls.service.dto.ManageViewDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.brighton.cls.domain.ManageView}.
 */
public interface ManageViewService {

    /**
     * Save a manageView.
     *
     * @param manageViewDTO the entity to save.
     * @return the persisted entity.
     */
    ManageViewDTO save(ManageViewDTO manageViewDTO);

    /**
     * Get all the manageViews.
     *
     * @return the list of entities.
     */
    List<ManageViewDTO> findAll();


    /**
     * Get the "id" manageView.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManageViewDTO> findOne(Long id);

    /**
     * Delete the "id" manageView.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
