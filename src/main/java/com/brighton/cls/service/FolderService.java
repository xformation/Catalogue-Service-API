package com.brighton.cls.service;

import com.brighton.cls.service.dto.FolderDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.brighton.cls.domain.Folder}.
 */
public interface FolderService {

    /**
     * Save a folder.
     *
     * @param folderDTO the entity to save.
     * @return the persisted entity.
     */
    FolderDTO save(FolderDTO folderDTO);

    /**
     * Get all the folders.
     *
     * @return the list of entities.
     */
    List<FolderDTO> findAll();


    /**
     * Get the "id" folder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FolderDTO> findOne(Long id);

    /**
     * Delete the "id" folder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
