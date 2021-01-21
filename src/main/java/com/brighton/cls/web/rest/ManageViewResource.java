package com.brighton.cls.web.rest;

import com.brighton.cls.service.ManageViewService;
import com.brighton.cls.web.rest.errors.BadRequestAlertException;
import com.brighton.cls.service.dto.ManageViewDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.brighton.cls.domain.ManageView}.
 */
@RestController
@RequestMapping("/api")
public class ManageViewResource {

    private final Logger log = LoggerFactory.getLogger(ManageViewResource.class);

    private static final String ENTITY_NAME = "catalogueserviceManageView";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManageViewService manageViewService;

    public ManageViewResource(ManageViewService manageViewService) {
        this.manageViewService = manageViewService;
    }

    /**
     * {@code POST  /manage-views} : Create a new manageView.
     *
     * @param manageViewDTO the manageViewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manageViewDTO, or with status {@code 400 (Bad Request)} if the manageView has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manage-views")
    public ResponseEntity<ManageViewDTO> createManageView(@Valid @RequestBody ManageViewDTO manageViewDTO) throws URISyntaxException {
        log.debug("REST request to save ManageView : {}", manageViewDTO);
        if (manageViewDTO.getId() != null) {
            throw new BadRequestAlertException("A new manageView cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManageViewDTO result = manageViewService.save(manageViewDTO);
        return ResponseEntity.created(new URI("/api/manage-views/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /manage-views} : Updates an existing manageView.
     *
     * @param manageViewDTO the manageViewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manageViewDTO,
     * or with status {@code 400 (Bad Request)} if the manageViewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manageViewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manage-views")
    public ResponseEntity<ManageViewDTO> updateManageView(@Valid @RequestBody ManageViewDTO manageViewDTO) throws URISyntaxException {
        log.debug("REST request to update ManageView : {}", manageViewDTO);
        if (manageViewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ManageViewDTO result = manageViewService.save(manageViewDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, manageViewDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /manage-views} : get all the manageViews.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manageViews in body.
     */
    @GetMapping("/manage-views")
    public List<ManageViewDTO> getAllManageViews() {
        log.debug("REST request to get all ManageViews");
        return manageViewService.findAll();
    }

    /**
     * {@code GET  /manage-views/:id} : get the "id" manageView.
     *
     * @param id the id of the manageViewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manageViewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manage-views/{id}")
    public ResponseEntity<ManageViewDTO> getManageView(@PathVariable Long id) {
        log.debug("REST request to get ManageView : {}", id);
        Optional<ManageViewDTO> manageViewDTO = manageViewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manageViewDTO);
    }

    /**
     * {@code DELETE  /manage-views/:id} : delete the "id" manageView.
     *
     * @param id the id of the manageViewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manage-views/{id}")
    public ResponseEntity<Void> deleteManageView(@PathVariable Long id) {
        log.debug("REST request to delete ManageView : {}", id);
        manageViewService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
