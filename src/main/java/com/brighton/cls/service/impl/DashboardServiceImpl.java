package com.brighton.cls.service.impl;

import com.brighton.cls.service.DashboardService;
import com.brighton.cls.domain.Dashboard;
import com.brighton.cls.repository.DashboardRepository;
import com.brighton.cls.service.dto.DashboardDTO;
import com.brighton.cls.service.mapper.DashboardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Dashboard}.
 */
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final DashboardRepository dashboardRepository;

    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(DashboardRepository dashboardRepository, DashboardMapper dashboardMapper) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardMapper = dashboardMapper;
    }

    /**
     * Save a dashboard.
     *
     * @param dashboardDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DashboardDTO save(DashboardDTO dashboardDTO) {
        log.debug("Request to save Dashboard : {}", dashboardDTO);
        Dashboard dashboard = dashboardMapper.toEntity(dashboardDTO);
        dashboard = dashboardRepository.save(dashboard);
        return dashboardMapper.toDto(dashboard);
    }

    /**
     * Get all the dashboards.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DashboardDTO> findAll() {
        log.debug("Request to get all Dashboards");
        return dashboardRepository.findAll().stream()
            .map(dashboardMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one dashboard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DashboardDTO> findOne(Long id) {
        log.debug("Request to get Dashboard : {}", id);
        return dashboardRepository.findById(id)
            .map(dashboardMapper::toDto);
    }

    /**
     * Delete the dashboard by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dashboard : {}", id);
        dashboardRepository.deleteById(id);
    }
}
