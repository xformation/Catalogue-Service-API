package com.brighton.cls.web.rest;

import com.brighton.cls.service.CollectorService;
import com.brighton.cls.web.rest.errors.BadRequestAlertException;
import com.brighton.cls.service.dto.CollectorDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.brighton.cls.domain.Collector}.
 */
@RestController
@RequestMapping("/api")
public class CollectorResource {

    private final Logger log = LoggerFactory.getLogger(CollectorResource.class);

    private static final String ENTITY_NAME = "catalogueserviceCollector";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectorService collectorService;

    public CollectorResource(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    /**
     * {@code POST  /collectors} : Create a new collector.
     *
     * @param collectorDTO the collectorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collectorDTO, or with status {@code 400 (Bad Request)} if the collector has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cls-collectors")
    public ResponseEntity<CollectorDTO> createCollector(@RequestBody CollectorDTO collectorDTO) throws URISyntaxException {
        log.debug("REST request to save Collector : {}", collectorDTO);
        if (collectorDTO.getId() != null) {
            throw new BadRequestAlertException("A new collector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollectorDTO result = collectorService.save(collectorDTO);
        return ResponseEntity.created(new URI("/api/collectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collectors} : Updates an existing collector.
     *
     * @param collectorDTO the collectorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectorDTO,
     * or with status {@code 400 (Bad Request)} if the collectorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collectorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cls-collectors")
    public ResponseEntity<CollectorDTO> updateCollector(@RequestBody CollectorDTO collectorDTO) throws URISyntaxException {
        log.debug("REST request to update Collector : {}", collectorDTO);
        if (collectorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CollectorDTO result = collectorService.save(collectorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, collectorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /collectors} : get all the collectors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectors in body.
     */
    @GetMapping("/cls-collectors")
    public List<CollectorDTO> getAllCollectors() {
        log.debug("REST request to get all Collectors");
        return collectorService.findAll();
    }

    /**
     * {@code GET  /collectors/:id} : get the "id" collector.
     *
     * @param id the id of the collectorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collectorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cls-collectors/{id}")
    public ResponseEntity<CollectorDTO> getCollector(@PathVariable Long id) {
        log.debug("REST request to get Collector : {}", id);
        Optional<CollectorDTO> collectorDTO = collectorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectorDTO);
    }

    /**
     * {@code DELETE  /collectors/:id} : delete the "id" collector.
     *
     * @param id the id of the collectorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cls-collectors/{id}")
    public ResponseEntity<Void> deleteCollector(@PathVariable Long id) {
        log.debug("REST request to delete Collector : {}", id);
        collectorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
