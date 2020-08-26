package com.brighton.cls.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    	List<FolderTree> folderTree = getFoldersTree();
    	List<LibraryTree> parentLibraryList = new ArrayList<>();
    	for(FolderTree ft: folderTree) {
    		
    		LibraryTree folderNode = new LibraryTree();
    		BeanUtils.copyProperties(ft, folderNode);
    		folderNode.setIsFolder(true);
    		folderNode.setHasChild(true);
    		folderNode.setName(ft.getTitle());
    		folderNode.setDescription(ft.getTitle()+" directory");
    		
    		Folder fl = new Folder();
    		fl.setId(ft.getId());
//    		BeanUtils.copyProperties(ft, fl);
    		
    		Library lb = new Library();
    		lb.setFolder(fl);
    		List<Library> libraryList = libraryRepository.findAll(Example.of(lb));
    		
    		LocalDateTime datetime = null;
    		for(Library library: libraryList) {
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
    		
    		parentLibraryList.add(folderNode);
    		getSubLibraryTree(ft, folderNode);
    	}
    	
    	LibraryTree finalTree = new LibraryTree();
    	finalTree.setName("Library");
    	finalTree.setIsFolder(true);
    	for(LibraryTree lt: parentLibraryList) {
    		logger.debug("Adding library object to root list. Library : ",lt);
    		finalTree.getItems().add(lt);
    	}
    	List<LibraryTree> finalList = new ArrayList<>();
    	finalList.add(finalTree);
    	return finalList;
    	
    }
    
    private void getSubLibraryTree(FolderTree ftObj, LibraryTree parentFolderNode) {
    	List<FolderTree> ftList = ftObj.getSubData();
    	LocalDateTime datetime = null;
    	for(FolderTree ft: ftList) {
    		LibraryTree folderNode = new LibraryTree();
    		BeanUtils.copyProperties(ft, folderNode);
    		folderNode.setIsFolder(true);
    		folderNode.setHasChild(true);
    		folderNode.setName(ft.getTitle());
    		folderNode.setDescription(ft.getTitle()+" directory");
    		
    		Folder fl = new Folder();
    		fl.setId(ft.getId());
//    		BeanUtils.copyProperties(ft, fl);
    		
    		Library lb = new Library();
    		lb.setFolder(fl);
    		List<Library> libraryList = libraryRepository.findAll(Example.of(lb));
    		
    		for(Library library: libraryList) {
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
    		
    		parentFolderNode.getItems().add(folderNode);
    		getSubLibraryTree(ft, folderNode);
    	}
    }
}
