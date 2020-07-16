package com.brighton.cls.web.rest;

import com.brighton.cls.service.LibraryFolderService;
import com.brighton.cls.web.rest.errors.BadRequestAlertException;
import com.brighton.cls.service.dto.LibraryFolderDTO;

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
 * REST controller for managing {@link com.brighton.cls.domain.LibraryFolder}.
 */
@RestController
@RequestMapping("/api")
public class LibraryFolderResource {

    private final Logger log = LoggerFactory.getLogger(LibraryFolderResource.class);

    private static final String ENTITY_NAME = "catalogueserviceLibraryFolder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LibraryFolderService libraryFolderService;

    public LibraryFolderResource(LibraryFolderService libraryFolderService) {
        this.libraryFolderService = libraryFolderService;
    }

    /**
     * {@code POST  /library-folders} : Create a new libraryFolder.
     *
     * @param libraryFolderDTO the libraryFolderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new libraryFolderDTO, or with status {@code 400 (Bad Request)} if the libraryFolder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/library-folders")
    public ResponseEntity<LibraryFolderDTO> createLibraryFolder(@RequestBody LibraryFolderDTO libraryFolderDTO) throws URISyntaxException {
        log.debug("REST request to save LibraryFolder : {}", libraryFolderDTO);
        if (libraryFolderDTO.getId() != null) {
            throw new BadRequestAlertException("A new libraryFolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LibraryFolderDTO result = libraryFolderService.save(libraryFolderDTO);
        return ResponseEntity.created(new URI("/api/library-folders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /library-folders} : Updates an existing libraryFolder.
     *
     * @param libraryFolderDTO the libraryFolderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libraryFolderDTO,
     * or with status {@code 400 (Bad Request)} if the libraryFolderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the libraryFolderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/library-folders")
    public ResponseEntity<LibraryFolderDTO> updateLibraryFolder(@RequestBody LibraryFolderDTO libraryFolderDTO) throws URISyntaxException {
        log.debug("REST request to update LibraryFolder : {}", libraryFolderDTO);
        if (libraryFolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LibraryFolderDTO result = libraryFolderService.save(libraryFolderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, libraryFolderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /library-folders} : get all the libraryFolders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of libraryFolders in body.
     */
    @GetMapping("/library-folders")
    public List<LibraryFolderDTO> getAllLibraryFolders() {
        log.debug("REST request to get all LibraryFolders");
        return libraryFolderService.findAll();
    }

    /**
     * {@code GET  /library-folders/:id} : get the "id" libraryFolder.
     *
     * @param id the id of the libraryFolderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the libraryFolderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/library-folders/{id}")
    public ResponseEntity<LibraryFolderDTO> getLibraryFolder(@PathVariable Long id) {
        log.debug("REST request to get LibraryFolder : {}", id);
        Optional<LibraryFolderDTO> libraryFolderDTO = libraryFolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(libraryFolderDTO);
    }

    /**
     * {@code DELETE  /library-folders/:id} : delete the "id" libraryFolder.
     *
     * @param id the id of the libraryFolderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/library-folders/{id}")
    public ResponseEntity<Void> deleteLibraryFolder(@PathVariable Long id) {
        log.debug("REST request to delete LibraryFolder : {}", id);
        libraryFolderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
