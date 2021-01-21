package com.brighton.cls.service.impl;

import com.brighton.cls.service.ManageViewService;
import com.brighton.cls.domain.ManageView;
import com.brighton.cls.repository.ManageViewRepository;
import com.brighton.cls.service.dto.ManageViewDTO;
import com.brighton.cls.service.mapper.ManageViewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ManageView}.
 */
@Service
@Transactional
public class ManageViewServiceImpl implements ManageViewService {

    private final Logger log = LoggerFactory.getLogger(ManageViewServiceImpl.class);

    private final ManageViewRepository manageViewRepository;

    private final ManageViewMapper manageViewMapper;

    public ManageViewServiceImpl(ManageViewRepository manageViewRepository, ManageViewMapper manageViewMapper) {
        this.manageViewRepository = manageViewRepository;
        this.manageViewMapper = manageViewMapper;
    }

    @Override
    public ManageViewDTO save(ManageViewDTO manageViewDTO) {
        log.debug("Request to save ManageView : {}", manageViewDTO);
        ManageView manageView = manageViewMapper.toEntity(manageViewDTO);
        manageView = manageViewRepository.save(manageView);
        return manageViewMapper.toDto(manageView);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManageViewDTO> findAll() {
        log.debug("Request to get all ManageViews");
        return manageViewRepository.findAll().stream()
            .map(manageViewMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ManageViewDTO> findOne(Long id) {
        log.debug("Request to get ManageView : {}", id);
        return manageViewRepository.findById(id)
            .map(manageViewMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ManageView : {}", id);
        manageViewRepository.deleteById(id);
    }
}
