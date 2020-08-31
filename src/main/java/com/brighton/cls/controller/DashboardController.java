package com.brighton.cls.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brighton.cls.config.Constants;
import com.brighton.cls.domain.CatalogDetail;
import com.brighton.cls.domain.Collector;
import com.brighton.cls.domain.Dashboard;
import com.brighton.cls.repository.CollectorRepository;
import com.brighton.cls.repository.DashboardRepository;

import io.github.jhipster.web.util.HeaderUtil;


/**
 * REST controller for managing {@link com.brighton.cls.domain.Dashboard}.
 */
@RestController
@RequestMapping("/api")
public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ENTITY_NAME = "dashboard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CollectorRepository collectorRepository;
    
    @Autowired
    private DashboardRepository dashboardRepository;
    
    /**
     * {@code POST  /addDashboardToCollector} : Add a dashboard to a existing collector.
     *
     * @param collectorId: collector's id.
     * @param dashboardJson: dashboard details in JSON string
     * @param dashboardDoc: dashboard's documentation
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body of dashboard, or with status {@code 400 (Bad Request)} if the collector id or dashboard json not provided.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/addDashboardToCollector")
    public ResponseEntity<Dashboard> addDashboardToCollector(
    										@RequestParam Long collectorId,
    										@RequestParam String dashboardName,
    										@RequestParam String dashboardJson, 
    										@RequestParam(required = false) String dashboardDoc,
    										@RequestParam (name = "userName", required = false) String userName) throws URISyntaxException {
        logger.info(String.format("Request to create add a dashboard to existing Collector. Collector id : %d", collectorId));
        
        Optional<Collector> oc = collectorRepository.findById(collectorId);
        if(!oc.isPresent()) {
        	logger.warn("Collector not found. Returning null");
        	return null;
        }
        
        Dashboard dashboard = new Dashboard();
        dashboard.setCollector(oc.get());
        dashboard.setName(dashboardName);
        dashboard.setDashboard(dashboardJson.getBytes());
        dashboard.setDescription(dashboardDoc);
        if(!StringUtils.isBlank(userName)) {
        	dashboard.setCreatedBy(userName);
        	dashboard.setUpdatedBy(userName);
    	}else {
    		dashboard.setCreatedBy(Constants.SYSTEM_ACCOUNT);
    		dashboard.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
    	}
    	Instant now = Instant.now();
    	dashboard.setCreatedOn(now);
    	dashboard.setUpdatedOn(now);
    	
        
        dashboard = dashboardRepository.save(dashboard);
        logger.info("Dashboard added to collector successfull");
        return ResponseEntity.created(new URI("/api/addDashboardToCollector/" + dashboard.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dashboard.getId().toString()))
            .body(dashboard);
    }

    /**
     * {@code DELETE  /deleteDashboardFromCollector} : delete a dashboard.
     *
     * @param id the id of the dashboard to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deleteDashboardFromCollector/{id}")
    public ResponseEntity<Void> deleteDashboardFromCollector(@PathVariable Long id) {
    	logger.info(String.format("Request to delete a dashboard. Dashboard id: %d", id));
        dashboardRepository.deleteById(id);
    	return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
    

    /**
     * {@code GET  /listDashboardOfCollector/{collectorId}} : get all the collectors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectors in body.
     */
    @GetMapping("/listDashboardOfCollector/{collectorId}")
    public List<Dashboard> listDashboardOfCollector(@PathVariable Long collectorId) {
        logger.info("Request to get all dashboards of a collector. Collector Id : ",collectorId);
        
        Optional<Collector> oc = collectorRepository.findById(collectorId);
        if(!oc.isPresent()) {
        	logger.warn("Collector not found. Returning empty list");
        	return Collections.emptyList();
        }
        
        Dashboard dashboard = new Dashboard();
        dashboard.setCollector(oc.get());
        
        return dashboardRepository.findAll(Example.of(dashboard), Sort.by(Direction.DESC, "id"));
    }

    /**
     * {@code GET  /listAllDashboard} : get list of all dashboards.
     *
     * @return the {@link List<Dashboard>} with status {@code 200 (OK)} and the list of dashboards.
     */
    @GetMapping("/listDashboard")
    public List<CatalogDetail> listAllDashboard(@RequestParam(required = false)  Long id, @RequestParam(required = false)  String isFolder) {
        if(!StringUtils.isBlank(isFolder) && !Objects.isNull(id)) {
        	logger.info("Request to get dashboards for id : "+id);
        	if(Boolean.valueOf(isFolder)) {
        		logger.info("Getting all dashboards of the given collector id : "+id);
        		Collector col = collectorRepository.findById(id).get();
        		Dashboard dashboard = new Dashboard();
        		dashboard.setCollector(col);
        		List<Dashboard> dashList = dashboardRepository.findAll(Example.of(dashboard));
        		List<CatalogDetail> catList = new ArrayList<>();
        		for(Dashboard d: dashList) {
        			CatalogDetail cd = new CatalogDetail();
        			cd.setId(d.getId());
        			cd.setTitle(d.getName());
        			cd.setDescription(d.getDescription());
        			cd.setDashboardJson(new String(d.getDashboard()));
        			catList.add(cd);
        		}
        		return catList;
        	}else {
        		logger.info("Getting a dashboard of the given id : "+id);
        		Dashboard d = dashboardRepository.findById(id).get();
        		CatalogDetail cd = new CatalogDetail();
    			cd.setId(d.getId());
    			cd.setTitle(d.getName());
    			cd.setDescription(d.getDescription());
    			cd.setDashboardJson(new String(d.getDashboard()));
    			List<CatalogDetail> list = new ArrayList<>();
        		list.add(cd);
        		return list;
        	}
        }
    	logger.info("Request to get all dashboards");
        List<Dashboard> dashList =  dashboardRepository.findAll(Sort.by(Direction.DESC, "id"));
        List<CatalogDetail> catList = new ArrayList<>();
		for(Dashboard d: dashList) {
			CatalogDetail cd = new CatalogDetail();
			cd.setId(d.getId());
			cd.setTitle(d.getName());
			cd.setDescription(d.getDescription());
			cd.setDashboardJson(new String(d.getDashboard()));
			catList.add(cd);
		}
		return catList;
    }

    /**
     * {@code GET  /getDashboard/:id} : get a dashboard for given id.
     *
     * @param id the id of the dashboard to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dashboard, or with status {@code 404 (Not Found)}.
     * @throws URISyntaxException 
     */
    @GetMapping("/getDashboard/{id}")
    public ResponseEntity<Dashboard> getDashboard(@PathVariable Long id) throws URISyntaxException {
        logger.debug("Request to get a dashboard. Dashboard id : ", id);
        Dashboard dashboard = dashboardRepository.findById(id).get();
        return ResponseEntity.created(new URI("/api/getDashboard/" + dashboard.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dashboard.getId().toString()))
                .body(dashboard);
    }
    
    /**
     * {@code DELETE  /deleteDashboard/:id} : delete the "id" dashboard.
     *
     * @param id the id of the dashboardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     * @throws URISyntaxException 
     */
    @DeleteMapping("/deleteDashboard/{id}")
    public ResponseEntity<Integer> deleteDashboard(@PathVariable Long id) throws URISyntaxException {
    	logger.debug("Request to delete Dashboard : Dashboard id : ", id);
        dashboardRepository.deleteById(id);
        return ResponseEntity.created(new URI("/api/deleteDashboard/" + id))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, id.toString()))
        .body(HttpStatus.OK.value());
    }
}
