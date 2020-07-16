package com.brighton.cls.service;

import com.brighton.cls.service.dto.LibraryFolderDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.brighton.cls.domain.LibraryFolder}.
 */
public interface LibraryFolderService {

    /**
     * Save a libraryFolder.
     *
     * @param libraryFolderDTO the entity to save.
     * @return the persisted entity.
     */
    LibraryFolderDTO save(LibraryFolderDTO libraryFolderDTO);

    /**
     * Get all the libraryFolders.
     *
     * @return the list of entities.
     */
    List<LibraryFolderDTO> findAll();


    /**
     * Get the "id" libraryFolder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LibraryFolderDTO> findOne(Long id);

    /**
     * Delete the "id" libraryFolder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
