package com.brighton.cls.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import com.brighton.cls.domain.Folder;
import com.brighton.cls.domain.FolderTree;
import com.brighton.cls.domain.Library;
import com.brighton.cls.repository.CollectorRepository;
import com.brighton.cls.repository.FolderRepository;
import com.brighton.cls.repository.LibraryRepository;

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
    public ResponseEntity<List<Folder>> addFolder(@RequestParam String title) throws URISyntaxException {
        logger.info(String.format("Request to create a folder. folder : %s", title));
    	Folder folder = new Folder();
    	folder.setTitle(title);
        
    	folder = folderRepository.save(folder);
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
    	List<FolderTree> parentList = new ArrayList<>();
        logger.debug("Request to get folders tree");
        List<Folder> folderList = folderRepository.findAll(Sort.by(Direction.DESC, "id"));
        
        for(Folder f: folderList) {
        	boolean hasChild = hasChildren(f);
            FolderTree node = new FolderTree();
        	node.setHasChild(hasChild);
        	if(Objects.isNull(f.getParentId())) {
        		BeanUtils.copyProperties(f, node);
        		parentList.add(node);
        	}
        }
        getTree(parentList);
        return parentList;
    }
    
    private void getTree(List<FolderTree> parentList) {
    	for(FolderTree ft: parentList) {
    		if(ft.getHasChild()) {
    			List<FolderTree> subList = getSubFolderList(ft.getId());
    			for(FolderTree cft: subList) {
    				Folder f = new Folder();
    				BeanUtils.copyProperties(cft, f);
    				boolean hasChild = hasChildren(f);
    				cft.setHasChild(hasChild);
    			}
    			ft.setSubData(subList);
    			getTree(subList);
    		}
    	}
    }
    
    private boolean hasChildren(Folder parent) {
		List<FolderTree> list = getSubFolderList(parent.getId());
		if(list.size() > 0) {
			return true;
		}
        return false;
    }
    
    private List<FolderTree> getSubFolderList(Long parentId){
    	Folder f = new Folder();
    	f.setParentId(parentId);
    	List<Folder> listF = this.folderRepository.findAll(Example.of(f), Sort.by(Direction.ASC, "title"));
    	List<FolderTree> childList = new ArrayList<>();
    	for(Folder fl: listF) {
    		FolderTree node = new FolderTree();
    		BeanUtils.copyProperties(fl, node);
    		childList.add(node);
    	}
    	return childList;
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
