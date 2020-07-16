package com.brighton.cls.service;

import com.brighton.cls.service.dto.LibraryDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.brighton.cls.domain.Library}.
 */
public interface LibraryService {

    /**
     * Save a library.
     *
     * @param libraryDTO the entity to save.
     * @return the persisted entity.
     */
    LibraryDTO save(LibraryDTO libraryDTO);

    /**
     * Get all the libraries.
     *
     * @return the list of entities.
     */
    List<LibraryDTO> findAll();


    /**
     * Get the "id" library.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LibraryDTO> findOne(Long id);

    /**
     * Delete the "id" library.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
