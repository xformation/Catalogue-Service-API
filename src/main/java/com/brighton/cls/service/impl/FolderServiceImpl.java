package com.brighton.cls.service.impl;

import com.brighton.cls.service.FolderService;
import com.brighton.cls.domain.Folder;
import com.brighton.cls.repository.FolderRepository;
import com.brighton.cls.service.dto.FolderDTO;
import com.brighton.cls.service.mapper.FolderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Folder}.
 */
@Service
@Transactional
public class FolderServiceImpl implements FolderService {

    private final Logger log = LoggerFactory.getLogger(FolderServiceImpl.class);

    private final FolderRepository folderRepository;

    private final FolderMapper folderMapper;

    public FolderServiceImpl(FolderRepository folderRepository, FolderMapper folderMapper) {
        this.folderRepository = folderRepository;
        this.folderMapper = folderMapper;
    }

    /**
     * Save a folder.
     *
     * @param folderDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FolderDTO save(FolderDTO folderDTO) {
        log.debug("Request to save Folder : {}", folderDTO);
        Folder folder = folderMapper.toEntity(folderDTO);
        folder = folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    /**
     * Get all the folders.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FolderDTO> findAll() {
        log.debug("Request to get all Folders");
        return folderRepository.findAll().stream()
            .map(folderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one folder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FolderDTO> findOne(Long id) {
        log.debug("Request to get Folder : {}", id);
        return folderRepository.findById(id)
            .map(folderMapper::toDto);
    }

    /**
     * Delete the folder by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Folder : {}", id);
        folderRepository.deleteById(id);
    }
}
