package com.brighton.cls.web.rest;

import com.brighton.cls.CatalogueserviceApp;
import com.brighton.cls.domain.LibraryFolder;
import com.brighton.cls.repository.LibraryFolderRepository;
import com.brighton.cls.service.LibraryFolderService;
import com.brighton.cls.service.dto.LibraryFolderDTO;
import com.brighton.cls.service.mapper.LibraryFolderMapper;

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
 * Integration tests for the {@link LibraryFolderResource} REST controller.
 */
@SpringBootTest(classes = CatalogueserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LibraryFolderResourceIT {

    private static final String DEFAULT_FOLDER = "AAAAAAAAAA";
    private static final String UPDATED_FOLDER = "BBBBBBBBBB";

    @Autowired
    private LibraryFolderRepository libraryFolderRepository;

    @Autowired
    private LibraryFolderMapper libraryFolderMapper;

    @Autowired
    private LibraryFolderService libraryFolderService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibraryFolderMockMvc;

    private LibraryFolder libraryFolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LibraryFolder createEntity(EntityManager em) {
        LibraryFolder libraryFolder = new LibraryFolder()
            .folder(DEFAULT_FOLDER);
        return libraryFolder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LibraryFolder createUpdatedEntity(EntityManager em) {
        LibraryFolder libraryFolder = new LibraryFolder()
            .folder(UPDATED_FOLDER);
        return libraryFolder;
    }

    @BeforeEach
    public void initTest() {
        libraryFolder = createEntity(em);
    }

    @Test
    @Transactional
    public void createLibraryFolder() throws Exception {
        int databaseSizeBeforeCreate = libraryFolderRepository.findAll().size();
        // Create the LibraryFolder
        LibraryFolderDTO libraryFolderDTO = libraryFolderMapper.toDto(libraryFolder);
        restLibraryFolderMockMvc.perform(post("/api/library-folders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(libraryFolderDTO)))
            .andExpect(status().isCreated());

        // Validate the LibraryFolder in the database
        List<LibraryFolder> libraryFolderList = libraryFolderRepository.findAll();
        assertThat(libraryFolderList).hasSize(databaseSizeBeforeCreate + 1);
        LibraryFolder testLibraryFolder = libraryFolderList.get(libraryFolderList.size() - 1);
        assertThat(testLibraryFolder.getFolder()).isEqualTo(DEFAULT_FOLDER);
    }

    @Test
    @Transactional
    public void createLibraryFolderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = libraryFolderRepository.findAll().size();

        // Create the LibraryFolder with an existing ID
        libraryFolder.setId(1L);
        LibraryFolderDTO libraryFolderDTO = libraryFolderMapper.toDto(libraryFolder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibraryFolderMockMvc.perform(post("/api/library-folders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(libraryFolderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LibraryFolder in the database
        List<LibraryFolder> libraryFolderList = libraryFolderRepository.findAll();
        assertThat(libraryFolderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLibraryFolders() throws Exception {
        // Initialize the database
        libraryFolderRepository.saveAndFlush(libraryFolder);

        // Get all the libraryFolderList
        restLibraryFolderMockMvc.perform(get("/api/library-folders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libraryFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].folder").value(hasItem(DEFAULT_FOLDER)));
    }
    
    @Test
    @Transactional
    public void getLibraryFolder() throws Exception {
        // Initialize the database
        libraryFolderRepository.saveAndFlush(libraryFolder);

        // Get the libraryFolder
        restLibraryFolderMockMvc.perform(get("/api/library-folders/{id}", libraryFolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(libraryFolder.getId().intValue()))
            .andExpect(jsonPath("$.folder").value(DEFAULT_FOLDER));
    }
    @Test
    @Transactional
    public void getNonExistingLibraryFolder() throws Exception {
        // Get the libraryFolder
        restLibraryFolderMockMvc.perform(get("/api/library-folders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLibraryFolder() throws Exception {
        // Initialize the database
        libraryFolderRepository.saveAndFlush(libraryFolder);

        int databaseSizeBeforeUpdate = libraryFolderRepository.findAll().size();

        // Update the libraryFolder
        LibraryFolder updatedLibraryFolder = libraryFolderRepository.findById(libraryFolder.getId()).get();
        // Disconnect from session so that the updates on updatedLibraryFolder are not directly saved in db
        em.detach(updatedLibraryFolder);
        updatedLibraryFolder
            .folder(UPDATED_FOLDER);
        LibraryFolderDTO libraryFolderDTO = libraryFolderMapper.toDto(updatedLibraryFolder);

        restLibraryFolderMockMvc.perform(put("/api/library-folders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(libraryFolderDTO)))
            .andExpect(status().isOk());

        // Validate the LibraryFolder in the database
        List<LibraryFolder> libraryFolderList = libraryFolderRepository.findAll();
        assertThat(libraryFolderList).hasSize(databaseSizeBeforeUpdate);
        LibraryFolder testLibraryFolder = libraryFolderList.get(libraryFolderList.size() - 1);
        assertThat(testLibraryFolder.getFolder()).isEqualTo(UPDATED_FOLDER);
    }

    @Test
    @Transactional
    public void updateNonExistingLibraryFolder() throws Exception {
        int databaseSizeBeforeUpdate = libraryFolderRepository.findAll().size();

        // Create the LibraryFolder
        LibraryFolderDTO libraryFolderDTO = libraryFolderMapper.toDto(libraryFolder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibraryFolderMockMvc.perform(put("/api/library-folders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(libraryFolderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LibraryFolder in the database
        List<LibraryFolder> libraryFolderList = libraryFolderRepository.findAll();
        assertThat(libraryFolderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLibraryFolder() throws Exception {
        // Initialize the database
        libraryFolderRepository.saveAndFlush(libraryFolder);

        int databaseSizeBeforeDelete = libraryFolderRepository.findAll().size();

        // Delete the libraryFolder
        restLibraryFolderMockMvc.perform(delete("/api/library-folders/{id}", libraryFolder.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LibraryFolder> libraryFolderList = libraryFolderRepository.findAll();
        assertThat(libraryFolderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
