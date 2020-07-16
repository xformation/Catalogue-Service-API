package com.brighton.cls.service.impl;

import com.brighton.cls.service.LibraryService;
import com.brighton.cls.domain.Library;
import com.brighton.cls.repository.LibraryRepository;
import com.brighton.cls.service.dto.LibraryDTO;
import com.brighton.cls.service.mapper.LibraryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Library}.
 */
@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private final Logger log = LoggerFactory.getLogger(LibraryServiceImpl.class);

    private final LibraryRepository libraryRepository;

    private final LibraryMapper libraryMapper;

    public LibraryServiceImpl(LibraryRepository libraryRepository, LibraryMapper libraryMapper) {
        this.libraryRepository = libraryRepository;
        this.libraryMapper = libraryMapper;
    }

    /**
     * Save a library.
     *
     * @param libraryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LibraryDTO save(LibraryDTO libraryDTO) {
        log.debug("Request to save Library : {}", libraryDTO);
        Library library = libraryMapper.toEntity(libraryDTO);
        library = libraryRepository.save(library);
        return libraryMapper.toDto(library);
    }

    /**
     * Get all the libraries.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LibraryDTO> findAll() {
        log.debug("Request to get all Libraries");
        return libraryRepository.findAll().stream()
            .map(libraryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one library by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LibraryDTO> findOne(Long id) {
        log.debug("Request to get Library : {}", id);
        return libraryRepository.findById(id)
            .map(libraryMapper::toDto);
    }

    /**
     * Delete the library by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Library : {}", id);
        libraryRepository.deleteById(id);
    }
}
