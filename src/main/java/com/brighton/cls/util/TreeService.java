package com.brighton.cls.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.brighton.cls.domain.CatalogDetail;
import com.brighton.cls.domain.Collector;
import com.brighton.cls.domain.Dashboard;
import com.brighton.cls.domain.Folder;
import com.brighton.cls.domain.FolderTree;
import com.brighton.cls.domain.Library;
import com.brighton.cls.domain.LibraryTree;
import com.brighton.cls.repository.CollectorRepository;
import com.brighton.cls.repository.DashboardRepository;
import com.brighton.cls.repository.FolderRepository;
import com.brighton.cls.repository.LibraryRepository;

@Service
public class TreeService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private CollectorRepository collectorRepository;
    
    @Autowired
    private DashboardRepository dashboardRepository;
    
    @Autowired
    private LibraryRepository libraryRepository;
    
	public List<FolderTree> getFoldersTree() {
    	List<FolderTree> parentList = new ArrayList<>();
        logger.debug("Getting folder tree");
        List<Folder> folderList = folderRepository.findAll(Sort.by(Direction.DESC, "id"));
        LocalDateTime datetime = null;
        for(Folder f: folderList) {
        	boolean hasChild = hasChildren(f);
            FolderTree node = new FolderTree();
        	node.setHasChild(hasChild);
        	if(Objects.isNull(f.getParentId())) {
        		BeanUtils.copyProperties(f, node);
//        		Instant in = f.getUpdatedOn();
        		datetime = LocalDateTime.ofInstant(f.getUpdatedOn(), ZoneId.systemDefault());
        		String formatedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(datetime);
        		node.setCreatedBy(f.getUpdatedBy());
        		node.setLastModified(formatedDate + " by "+ f.getUpdatedBy());
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
    	LocalDateTime datetime = null;
    	for(Folder fl: listF) {
    		FolderTree node = new FolderTree();
    		BeanUtils.copyProperties(fl, node);
    		datetime = LocalDateTime.ofInstant(fl.getUpdatedOn(), ZoneId.systemDefault());
    		String formatedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(datetime);
    		node.setCreatedBy(fl.getUpdatedBy());
    		node.setLastModified(formatedDate + " by "+ fl.getUpdatedBy());
    		childList.add(node);
    	}
    	return childList;
    }
    
    
    public List<LibraryTree> getLibraryTree() {
        logger.debug("Request to get library tree");
        List<Library> libraryList = libraryRepository.findAll(Sort.by(Direction.DESC, "id"));
        Map<Long, LibraryTree> orgMap = new HashMap<Long, LibraryTree>();  
        Map<Long, LibraryTree> mp = new HashMap<>();
        LocalDateTime datetime = null;
        for(Library library: libraryList) {
        	LibraryTree folderNode = null;
        	if(!orgMap.containsKey(library.getFolder().getId())) {
        		folderNode = new LibraryTree();
        		folderNode.setId(library.getFolder().getId());
        		folderNode.setParentId(library.getFolder().getParentId());
        		folderNode.setIsFolder(true);
        		folderNode.setHasChild(true);
        		folderNode.setName(library.getFolder().getTitle());
        		folderNode.setDescription(library.getFolder().getTitle()+" directory");
        		
        		datetime = LocalDateTime.ofInstant(library.getFolder().getUpdatedOn(), ZoneId.systemDefault());
        		String formatedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(datetime);
        		folderNode.setCreatedBy(library.getFolder().getUpdatedBy());
        		folderNode.setLastModified(formatedDate + " by "+ library.getFolder().getUpdatedBy());
        		
        		orgMap.put(library.getFolder().getId(), folderNode);
        	}else {
        		folderNode = orgMap.get(library.getFolder().getId());
        	}
        	Collector col = library.getCollector();
    		LibraryTree collectorNode = new LibraryTree();
    		collectorNode.setId(col.getId());
    		collectorNode.setParentId(folderNode.getId());
    		collectorNode.setName(col.getName());
    		collectorNode.setDescription(col.getDescription());
    		collectorNode.setIsFolder(false);
    		collectorNode.setHasChild(false);
    		
    		datetime = LocalDateTime.ofInstant(col.getUpdatedOn(), ZoneId.systemDefault());
    		String formatedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(datetime);
    		collectorNode.setCreatedBy(col.getUpdatedBy());
    		collectorNode.setLastModified(formatedDate + " by "+ col.getUpdatedBy());
    		
    		Dashboard dashboard = new Dashboard();
    		dashboard.setCollector(col);
    		List<Dashboard> dsList = dashboardRepository.findAll(Example.of(dashboard));
    		for(Dashboard d: dsList) {
    			CatalogDetail cd = new CatalogDetail();
    			cd.setId(d.getId());
    			cd.setTitle(d.getName());
    			cd.setDescription(d.getDescription());
    			collectorNode.getDashboardList().add(cd);
    		}
    		folderNode.getItems().add(collectorNode);
        }
        
        List<LibraryTree> parentList = new ArrayList<>();
        
        for(LibraryTree lt : orgMap.values()) {
        	getSubLibraryTree(lt, parentList, mp);
        }
        
        List<LibraryTree> reList = new ArrayList<>();
        for(LibraryTree lt: mp.values()) {
        	logger.debug("Parent id : "+lt.getParentId()+", Id: "+lt.getId()+", name : "+lt.getName());
        	if(!Objects.isNull(lt.getParentId())) {
        		LibraryTree parentObj = mp.get(lt.getParentId());
        		boolean isFound = false;
        		for(LibraryTree childObj: parentObj.getItems()) {
        			if(lt.getId().compareTo(childObj.getId()) == 0 ) {
        				isFound = true;
        			}
        		}
        		if(!isFound) {
        			parentObj.getItems().add(lt);
        		}
        	}else {
        		reList.add(lt);
        	}
        }
        
        LibraryTree topNode = new LibraryTree();
        topNode.setName("Library");
        topNode.setIsFolder(true);

        for(LibraryTree lt: reList) {
        	topNode.getItems().add(lt);
        }
        
        List<LibraryTree> finalList = new ArrayList<>();
        finalList.add(topNode);
        return finalList;
    }
    
    private void getSubLibraryTree(LibraryTree childNode, List<LibraryTree> parentList, Map<Long, LibraryTree> mp) {
    	mp.put(childNode.getId(), childNode);
    	LocalDateTime datetime = null;
    	if(!Objects.isNull(childNode.getParentId())) {
    		Folder parentFolder = folderRepository.findById(childNode.getParentId()).get();
    		LibraryTree parentNode = new LibraryTree();
    		
    		parentNode.setId(parentFolder.getId());
    		parentNode.setParentId(parentFolder.getParentId());
    		parentNode.setIsFolder(true);
    		parentNode.setHasChild(true);
    		parentNode.setName(parentFolder.getTitle());
    		parentNode.setDescription(parentFolder.getTitle()+" directory");
    		
    		datetime = LocalDateTime.ofInstant(parentFolder.getUpdatedOn(), ZoneId.systemDefault());
    		String formatedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(datetime);
    		parentNode.setCreatedBy(parentFolder.getUpdatedBy());
    		parentNode.setLastModified(formatedDate + " by "+ parentFolder.getUpdatedBy());
    		
    		logger.debug("Adding child to the parent");
    		parentNode.getItems().add(childNode);
    		
    		getSubLibraryTree(parentNode, parentList, mp);
    		
    	}else {
    		parentList.add(childNode);
    	}
    }
}
