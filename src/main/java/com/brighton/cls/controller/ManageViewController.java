package com.brighton.cls.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brighton.cls.config.Constants;
import com.brighton.cls.domain.ManageView;
import com.brighton.cls.domain.ManageViewDetail;
import com.brighton.cls.repository.ManageViewRepository;

import io.github.jhipster.web.util.HeaderUtil;


/**
 * REST controller for managing {@link com.brighton.cls.domain.ManageView}.
 */
@RestController
@RequestMapping("/api")
public class ManageViewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ENTITY_NAME = "manage_view";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ManageViewRepository manageViewRepository;
    
    @PostMapping("/addView")
    public ResponseEntity<ManageView> addView(
    										@RequestParam String viewName,
    										@RequestParam String viewJson, 
    										@RequestParam(required = false) String description,
    										@RequestParam(required = false) String type,
    										@RequestParam(required = false) String status,
    										@RequestParam (name = "userName", required = false) String userName) throws URISyntaxException {
        logger.info(String.format("Request to add a view"));
        
        ManageView view = new ManageView();
        view.setName(viewName);
        view.setViewData(viewJson.getBytes());
        view.setDescription(description);
        view.setType(type);
        view.setStatus(status);
        
        if(!StringUtils.isBlank(userName)) {
        	view.setCreatedBy(userName);
        	view.setUpdatedBy(userName);
    	}else {
    		view.setCreatedBy(Constants.SYSTEM_ACCOUNT);
    		view.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
    	}
        
    	Instant now = Instant.now();
    	view.setCreatedOn(now);
    	view.setUpdatedOn(now);
        
        view = manageViewRepository.save(view);
        logger.info("View created successfully");
        return ResponseEntity.created(new URI("/api/addView/" + view.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, view.getId().toString()))
            .body(view);
    }
    
    @PostMapping("/updateView")
    public ResponseEntity<ManageView> updateView(
    										@RequestParam Long id,
    										@RequestParam(required = false) String viewName,
    										@RequestParam(required = false) String viewJson, 
    										@RequestParam(required = false) String description,
    										@RequestParam(required = false) String type,
    										@RequestParam(required = false) String status,
    										@RequestParam (name = "userName", required = false) String userName) throws URISyntaxException {
        logger.info(String.format("Request to update a view"));
        
        Optional<ManageView> oc = manageViewRepository.findById(id);
        if(!oc.isPresent()) {
        	logger.warn("View not found. Returning null");
        	return null;
        }
        
        ManageView view = oc.get();
        if(!StringUtils.isBlank(viewName)) {
        	view.setName(viewName);
        }
        if(!StringUtils.isBlank(viewJson)) {
        	view.setViewData(viewJson.getBytes());
        }
        if(!StringUtils.isBlank(description)) {
        	view.setDescription(description);
        }
        if(!StringUtils.isBlank(type)) {
        	view.setType(type);
        }
        if(!StringUtils.isBlank(type)) {
        	view.setStatus(status);
        }
        
        if(!StringUtils.isBlank(userName)) {
        	view.setUpdatedBy(userName);
    	}else {
    		view.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
    	}
        
    	Instant now = Instant.now();
    	view.setUpdatedOn(now);
        
        view = manageViewRepository.save(view);
        logger.info("View updated successfully");
        return ResponseEntity.created(new URI("/api/updateView/" + view.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, view.getId().toString()))
            .body(view);
    }
    
    @DeleteMapping("/deleteView/{id}")
    public ResponseEntity<Void> deleteView(@PathVariable Long id) {
    	logger.info(String.format("Request to delete a view. View id: %d", id));
        manageViewRepository.deleteById(id);
    	return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/listView")
    public List<ManageViewDetail> listAllView() {
    	logger.info("Request to get all views");
        List<ManageView> list =  manageViewRepository.findAll(Sort.by(Direction.DESC, "id"));
        List<ManageViewDetail> mvList = new ArrayList<>();
		for(ManageView mv: list) {
			ManageViewDetail mvd = new ManageViewDetail();
			mvd.setId(mv.getId());
			mvd.setName(mv.getName());
			mvd.setDescription(mv.getDescription());
			mvd.setViewJson(new String(mv.getViewData()));
			mvList.add(mvd);
		}
		return mvList;
    }

    @GetMapping("/getView/{id}")
    public ResponseEntity<ManageViewDetail> getView(@PathVariable Long id) throws URISyntaxException {
        logger.debug("Request to get a view. View id : ", id);
        ManageView mv = manageViewRepository.findById(id).get();
        ManageViewDetail mvd = new ManageViewDetail();
		mvd.setId(mv.getId());
		mvd.setName(mv.getName());
		mvd.setDescription(mv.getDescription());
		mvd.setViewJson(new String(mv.getViewData()));
		
        return ResponseEntity.created(new URI("/api/getView/" + mv.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, mv.getId().toString()))
                .body(mvd);
    }
    

}
