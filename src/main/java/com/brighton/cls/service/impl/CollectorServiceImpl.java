package com.brighton.cls.service.impl;

import com.brighton.cls.service.CollectorService;
import com.brighton.cls.domain.Collector;
import com.brighton.cls.repository.CollectorRepository;
import com.brighton.cls.service.dto.CollectorDTO;
import com.brighton.cls.service.mapper.CollectorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Collector}.
 */
@Service
@Transactional
public class CollectorServiceImpl implements CollectorService {

    private final Logger log = LoggerFactory.getLogger(CollectorServiceImpl.class);

    private final CollectorRepository collectorRepository;

    private final CollectorMapper collectorMapper;

    public CollectorServiceImpl(CollectorRepository collectorRepository, CollectorMapper collectorMapper) {
        this.collectorRepository = collectorRepository;
        this.collectorMapper = collectorMapper;
    }

    /**
     * Save a collector.
     *
     * @param collectorDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CollectorDTO save(CollectorDTO collectorDTO) {
        log.debug("Request to save Collector : {}", collectorDTO);
        Collector collector = collectorMapper.toEntity(collectorDTO);
        collector = collectorRepository.save(collector);
        return collectorMapper.toDto(collector);
    }

    /**
     * Get all the collectors.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollectorDTO> findAll() {
        log.debug("Request to get all Collectors");
        return collectorRepository.findAll().stream()
            .map(collectorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one collector by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CollectorDTO> findOne(Long id) {
        log.debug("Request to get Collector : {}", id);
        return collectorRepository.findById(id)
            .map(collectorMapper::toDto);
    }

    /**
     * Delete the collector by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Collector : {}", id);
        collectorRepository.deleteById(id);
    }
}
