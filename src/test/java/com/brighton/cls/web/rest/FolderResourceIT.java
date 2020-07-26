package com.brighton.cls.web.rest;

import com.brighton.cls.CatalogueserviceApp;
import com.brighton.cls.domain.Folder;
import com.brighton.cls.repository.FolderRepository;
import com.brighton.cls.service.FolderService;
import com.brighton.cls.service.dto.FolderDTO;
import com.brighton.cls.service.mapper.FolderMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FolderResource} REST controller.
 */
@SpringBootTest(classes = CatalogueserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FolderResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final Boolean DEFAULT_IS_OPENED = false;
    private static final Boolean UPDATED_IS_OPENED = true;

    private static final Boolean DEFAULT_IS_CHECKED = false;
    private static final Boolean UPDATED_IS_CHECKED = true;

    private static final Boolean DEFAULT_IS_FOLDER = false;
    private static final Boolean UPDATED_IS_FOLDER = true;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private FolderService folderService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFolderMockMvc;

    private Folder folder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Folder createEntity(EntityManager em) {
        Folder folder = new Folder()
            .title(DEFAULT_TITLE)
            .parentId(DEFAULT_PARENT_ID)
            .isOpened(DEFAULT_IS_OPENED)
            .isChecked(DEFAULT_IS_CHECKED)
            .isFolder(DEFAULT_IS_FOLDER);
        return folder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Folder createUpdatedEntity(EntityManager em) {
        Folder folder = new Folder()
            .title(UPDATED_TITLE)
            .parentId(UPDATED_PARENT_ID)
            .isOpened(UPDATED_IS_OPENED)
            .isChecked(UPDATED_IS_CHECKED)
            .isFolder(UPDATED_IS_FOLDER);
        return folder;
    }

    @BeforeEach
    public void initTest() {
        folder = createEntity(em);
    }

    @Test
    @Transactional
    public void createFolder() throws Exception {
        int databaseSizeBeforeCreate = folderRepository.findAll().size();
        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);
        restFolderMockMvc.perform(post("/api/folders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isCreated());

        // Validate the Folder in the database
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeCreate + 1);
        Folder testFolder = folderList.get(folderList.size() - 1);
        assertThat(testFolder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFolder.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testFolder.isIsOpened()).isEqualTo(DEFAULT_IS_OPENED);
        assertThat(testFolder.isIsChecked()).isEqualTo(DEFAULT_IS_CHECKED);
        assertThat(testFolder.isIsFolder()).isEqualTo(DEFAULT_IS_FOLDER);
    }

    @Test
    @Transactional
    public void createFolderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = folderRepository.findAll().size();

        // Create the Folder with an existing ID
        folder.setId(1L);
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFolderMockMvc.perform(post("/api/folders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFolders() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);

        // Get all the folderList
        restFolderMockMvc.perform(get("/api/folders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(folder.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].isOpened").value(hasItem(DEFAULT_IS_OPENED.booleanValue())))
            .andExpect(jsonPath("$.[*].isChecked").value(hasItem(DEFAULT_IS_CHECKED.booleanValue())))
            .andExpect(jsonPath("$.[*].isFolder").value(hasItem(DEFAULT_IS_FOLDER.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getFolder() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);

        // Get the folder
        restFolderMockMvc.perform(get("/api/folders/{id}", folder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(folder.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.isOpened").value(DEFAULT_IS_OPENED.booleanValue()))
            .andExpect(jsonPath("$.isChecked").value(DEFAULT_IS_CHECKED.booleanValue()))
            .andExpect(jsonPath("$.isFolder").value(DEFAULT_IS_FOLDER.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFolder() throws Exception {
        // Get the folder
        restFolderMockMvc.perform(get("/api/folders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFolder() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);

        int databaseSizeBeforeUpdate = folderRepository.findAll().size();

        // Update the folder
        Folder updatedFolder = folderRepository.findById(folder.getId()).get();
        // Disconnect from session so that the updates on updatedFolder are not directly saved in db
        em.detach(updatedFolder);
        updatedFolder
            .title(UPDATED_TITLE)
            .parentId(UPDATED_PARENT_ID)
            .isOpened(UPDATED_IS_OPENED)
            .isChecked(UPDATED_IS_CHECKED)
            .isFolder(UPDATED_IS_FOLDER);
        FolderDTO folderDTO = folderMapper.toDto(updatedFolder);

        restFolderMockMvc.perform(put("/api/folders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isOk());

        // Validate the Folder in the database
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeUpdate);
        Folder testFolder = folderList.get(folderList.size() - 1);
        assertThat(testFolder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFolder.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testFolder.isIsOpened()).isEqualTo(UPDATED_IS_OPENED);
        assertThat(testFolder.isIsChecked()).isEqualTo(UPDATED_IS_CHECKED);
        assertThat(testFolder.isIsFolder()).isEqualTo(UPDATED_IS_FOLDER);
    }

    @Test
    @Transactional
    public void updateNonExistingFolder() throws Exception {
        int databaseSizeBeforeUpdate = folderRepository.findAll().size();

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFolderMockMvc.perform(put("/api/folders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFolder() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);

        int databaseSizeBeforeDelete = folderRepository.findAll().size();

        // Delete the folder
        restFolderMockMvc.perform(delete("/api/folders/{id}", folder.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
