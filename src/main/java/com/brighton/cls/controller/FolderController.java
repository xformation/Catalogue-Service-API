package com.brighton.cls.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brighton.cls.config.Constants;
import com.brighton.cls.domain.Folder;
import com.brighton.cls.domain.FolderTree;
import com.brighton.cls.domain.Library;
import com.brighton.cls.repository.FolderRepository;
import com.brighton.cls.repository.LibraryRepository;
import com.brighton.cls.util.TreeService;

import io.github.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.brighton.cls.domain.Folder}.
 */
@RestController
@RequestMapping("/api")
public class FolderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ENTITY_NAME = "folder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private LibraryRepository libraryRepository;
    
    @Autowired
    private TreeService treeService;
    
    /**
     * {@code POST  /addFolder} : Create a new folder.
     *
     * @param folder: virtual folders.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the list of folders, or with status {@code 400 (Bad Request)} if folder (path) not provided.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/addFolder")
    public ResponseEntity<List<Folder>> addFolder(@RequestParam String title, 
    		@RequestParam (name = "parentId", required = false) Long parentId,
    		@RequestParam (name = "userName", required = false) String userName) throws URISyntaxException {
        logger.info(String.format("Request to create a folder. folder : %s", title));
    	Folder folder = new Folder();
    	folder.setTitle(title);
    	if(!StringUtils.isBlank(userName)) {
    		folder.setCreatedBy(userName);
    		folder.setUpdatedBy(userName);
    	}else {
    		folder.setCreatedBy(Constants.SYSTEM_ACCOUNT);
    		folder.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
    	}
    	Instant now = Instant.now();
    	folder.setCreatedOn(now);
    	folder.setUpdatedOn(now);
        if(!Objects.isNull(parentId)) {
        	Optional<Folder> of = this.folderRepository.findById(parentId);
        	if(of.isPresent()) {
        		folder.setParentId(parentId);
        		folder = folderRepository.save(folder);
        		logger.debug("Folder created : ", folder);
        	}else {
        		logger.warn("Invalid parent id. Cannot save this record");
        	}
        }else {
        	logger.debug("No parent provided. Its a root folder");
        	folder = folderRepository.save(folder);
        }
    	
        List<Folder> list = getAllFolders();
        return ResponseEntity.created(new URI("/api/addFolder/" + folder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, folder.getId().toString()))
            .body(list);
        
    }

    /**
     * {@code GET  /listFolder} : get all the folders.
     *
     * @return the {@link List<Folder>} with status {@code 200 (OK)} and the list of folders in body.
     */
    @GetMapping("/listFolder")
    public List<Folder> getAllFolders() {
        logger.debug("Request to get all Folders");
        return folderRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    
    @GetMapping("/listFolderTree")
    public List<FolderTree> getFoldersTree() {
        logger.debug("Request to get folders tree");
        return treeService.getFoldersTree();
    }
        
    /**
     * {@code GET  /listCollectorOfFolder/{folder}} : get all the collectors of a folder.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectors in body.
     */
    @GetMapping("/listCollectorOfFolder/{folder}")
    public List<Library> listCollectorOfFolder(@PathVariable String title) {
        logger.info("Request to get all collectors of a library folder. folder : ",title);
        Folder lf = new Folder();
        lf.setTitle(title);
        Optional<Folder> olf = folderRepository.findOne(Example.of(lf));
        if(!olf.isPresent()) {
        	logger.warn("Folder not found. Returning empty list");
        	return Collections.emptyList();
        }
        
        Library library = new Library();
        library.setFolder(olf.get());
        List<Library> list = libraryRepository.findAll(Example.of(library), Sort.by(Direction.DESC, "id"));
        
        return list;
    }
}
