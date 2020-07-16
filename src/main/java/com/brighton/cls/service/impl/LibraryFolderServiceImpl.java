package com.brighton.cls.service.impl;

import com.brighton.cls.service.LibraryFolderService;
import com.brighton.cls.domain.LibraryFolder;
import com.brighton.cls.repository.LibraryFolderRepository;
import com.brighton.cls.service.dto.LibraryFolderDTO;
import com.brighton.cls.service.mapper.LibraryFolderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link LibraryFolder}.
 */
@Service
@Transactional
public class LibraryFolderServiceImpl implements LibraryFolderService {

    private final Logger log = LoggerFactory.getLogger(LibraryFolderServiceImpl.class);

    private final LibraryFolderRepository libraryFolderRepository;

    private final LibraryFolderMapper libraryFolderMapper;

    public LibraryFolderServiceImpl(LibraryFolderRepository libraryFolderRepository, LibraryFolderMapper libraryFolderMapper) {
        this.libraryFolderRepository = libraryFolderRepository;
        this.libraryFolderMapper = libraryFolderMapper;
    }

    /**
     * Save a libraryFolder.
     *
     * @param libraryFolderDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LibraryFolderDTO save(LibraryFolderDTO libraryFolderDTO) {
        log.debug("Request to save LibraryFolder : {}", libraryFolderDTO);
        LibraryFolder libraryFolder = libraryFolderMapper.toEntity(libraryFolderDTO);
        libraryFolder = libraryFolderRepository.save(libraryFolder);
        return libraryFolderMapper.toDto(libraryFolder);
    }

    /**
     * Get all the libraryFolders.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LibraryFolderDTO> findAll() {
        log.debug("Request to get all LibraryFolders");
        return libraryFolderRepository.findAll().stream()
            .map(libraryFolderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one libraryFolder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LibraryFolderDTO> findOne(Long id) {
        log.debug("Request to get LibraryFolder : {}", id);
        return libraryFolderRepository.findById(id)
            .map(libraryFolderMapper::toDto);
    }

    /**
     * Delete the libraryFolder by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LibraryFolder : {}", id);
        libraryFolderRepository.deleteById(id);
    }
}
