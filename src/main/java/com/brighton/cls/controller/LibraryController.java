package com.brighton.cls.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brighton.cls.domain.Collector;
import com.brighton.cls.domain.Folder;
import com.brighton.cls.domain.Library;
import com.brighton.cls.domain.LibraryTree;
import com.brighton.cls.repository.CollectorRepository;
import com.brighton.cls.repository.FolderRepository;
import com.brighton.cls.repository.LibraryRepository;
import com.brighton.cls.util.TreeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    private FolderRepository folderRepository;
    
    @Autowired
    private CollectorRepository collectorRepository;
    
    @Autowired
    private LibraryRepository libraryRepository;
    
    @Autowired
    private TreeService treeService;
    
    /**
     * {@code POST  /addCollectorToLibrary} : add an entry in library.
     *
     * @param collectorId: collector's id.
     * @param folderId: folder's id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new library,
     * or with status {@code 417 Exception_Failed} if the collectorId/folderId is null,
     */
    @PostMapping("/addCollectorToLibrary")
    public HttpStatus addCollectorToLibrary(@RequestBody ObjectNode obj) {
    	logger.info("Request to add a Collector to library. Request object : "+obj);
        try {
        	Optional<Collector> oc = collectorRepository.findById(obj.get("collectorId").asLong());
            String appName = obj.get("appName").asText();
            String dataSource = obj.get("dataSource").asText();
            
            Iterator<JsonNode> itr = obj.get("folderIdList").elements();
            while (itr.hasNext()) {
    			JsonNode folderId = itr.next();
    	        
    			Optional<Folder> olf = folderRepository.findById(folderId.asLong());
    	        
    	        Library library = new Library();
    	        library.setCollector(oc.get());
    	        library.setFolder(olf.get());
    	        library.setAppName(appName);
    	        library.setDataSource(dataSource);
    	        library = libraryRepository.save(library);
    		}
        }catch(Exception e) {
        	logger.error("Error adding collector to library : ",e);
        	return HttpStatus.EXPECTATION_FAILED;
        }
        return HttpStatus.OK;
    }
    
    /**
     * {@code POST  /addFolderToLibrary} : add an entry in library.
     *
     * @param folderId: folder's id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new library,
     * or with status {@code 400 (Bad Request)} if folderId is null,
     * or with status {@code 500 (Internal Server Error)} if the library couldn't be added.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/addFolderToLibrary")
    public ResponseEntity<Library> addFolderToLibrary(@RequestParam Long folderId) throws URISyntaxException {
        logger.info(String.format("Request to add a folder to library. folder id : %d", folderId));
        
        Optional<Folder> olf = folderRepository.findById(folderId);
        
        Library library = new Library();
        library.setFolder(olf.get());
        
        library = libraryRepository.save(library);
        
        return ResponseEntity.created(new URI("/api/addFolderToLibrary/" + library.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, library.getId().toString()))
            .body(library);
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
    
    @GetMapping("/listLibraryTree")
    public List<LibraryTree> getLibraryTree() {
    	return treeService.getLibraryTree();
    }
    
}
