package com.brighton.cls.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brighton.cls.domain.Collector;
import com.brighton.cls.domain.Library;
import com.brighton.cls.domain.LibraryFolder;
import com.brighton.cls.repository.CollectorRepository;
import com.brighton.cls.repository.LibraryFolderRepository;
import com.brighton.cls.repository.LibraryRepository;

import io.github.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.brighton.cls.domain.Collector}.
 */
@RestController
@RequestMapping("/api")
public class LibraryController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ENTITY_NAME = "collector";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private LibraryFolderRepository libraryFolderRepository;
    
    @Autowired
    private CollectorRepository collectorRepository;
    
    @Autowired
    private LibraryRepository libraryRepository;
    
    /**
     * {@code POST  /addFolder} : Create a new folder.
     *
     * @param folder: virtual folders.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the list of folders, or with status {@code 400 (Bad Request)} if folder (path) not provided.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/addFolder")
    public ResponseEntity<List<LibraryFolder>> addFolder(@RequestParam String folder) throws URISyntaxException {
        logger.info(String.format("Request to create a folder. folder : %s", folder));
    	LibraryFolder libraryFolder = new LibraryFolder();
    	libraryFolder.setFolder(folder);
        
    	libraryFolder = libraryFolderRepository.save(libraryFolder);
        List<LibraryFolder> list = getAllFolders();
        return ResponseEntity.created(new URI("/api/addFolder/" + libraryFolder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, libraryFolder.getId().toString()))
            .body(list);
        
    }

    /**
     * {@code POST  /addCollectorToLibrary} : add an entry in library.
     *
     * @param collectorId: collector's id.
     * @param folderId: folder's id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new library,
     * or with status {@code 400 (Bad Request)} if the collectorId/folderId is null,
     * or with status {@code 500 (Internal Server Error)} if the library couldn't be added.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/addCollectorToLibrary")
    public ResponseEntity<Library> addCollectorToLibrary(@RequestParam Long collectorId, @RequestParam Long folderId) throws URISyntaxException {
        logger.info(String.format("Request to add a Collector to library. Collector id : %d, folder id : %d", collectorId, folderId));
        
        Optional<Collector> oc = collectorRepository.findById(collectorId);
        Optional<LibraryFolder> olf = libraryFolderRepository.findById(folderId);
        
        Library library = new Library();
        library.setCollector(oc.get());
        library.setLibraryFolder(olf.get());
        
        library = libraryRepository.save(library);
        
        return ResponseEntity.created(new URI("/api/addCollectorToLibrary/" + library.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, library.getId().toString()))
            .body(library);
    }
    
    /**
     * {@code GET  /listFolder} : get all the folders.
     *
     * @return the {@link List<LibraryFolder>} with status {@code 200 (OK)} and the list of folders in body.
     */
    @GetMapping("/listFolder")
    public List<LibraryFolder> getAllFolders() {
        logger.debug("Request to get all Folders");
        return libraryFolderRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    /**
     * {@code GET  /listLibrary} : get all the library.
     *
     * @return the {@link List<Library>} with status {@code 200 (OK)} and the list of library in body.
     */
    @GetMapping("/listLibrary")
    public List<Library> getAllLibrary() {
        logger.debug("Request to get all library");
        return libraryRepository.findAll(Sort.by(Direction.DESC, "id"));
    }
    
}
