package com.brighton.cls.web.rest;

import com.brighton.cls.CatalogueserviceApp;
import com.brighton.cls.domain.Library;
import com.brighton.cls.repository.LibraryRepository;
import com.brighton.cls.service.LibraryService;
import com.brighton.cls.service.dto.LibraryDTO;
import com.brighton.cls.service.mapper.LibraryMapper;

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
 * Integration tests for the {@link LibraryResource} REST controller.
 */
@SpringBootTest(classes = CatalogueserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LibraryResourceIT {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibraryMockMvc;

    private Library library;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Library createEntity(EntityManager em) {
        Library library = new Library();
        return library;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Library createUpdatedEntity(EntityManager em) {
        Library library = new Library();
        return library;
    }

    @BeforeEach
    public void initTest() {
        library = createEntity(em);
    }

    @Test
    @Transactional
    public void createLibrary() throws Exception {
        int databaseSizeBeforeCreate = libraryRepository.findAll().size();
        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);
        restLibraryMockMvc.perform(post("/api/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
            .andExpect(status().isCreated());

        // Validate the Library in the database
        List<Library> libraryList = libraryRepository.findAll();
        assertThat(libraryList).hasSize(databaseSizeBeforeCreate + 1);
        Library testLibrary = libraryList.get(libraryList.size() - 1);
    }

    @Test
    @Transactional
    public void createLibraryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = libraryRepository.findAll().size();

        // Create the Library with an existing ID
        library.setId(1L);
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibraryMockMvc.perform(post("/api/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        List<Library> libraryList = libraryRepository.findAll();
        assertThat(libraryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLibraries() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        // Get all the libraryList
        restLibraryMockMvc.perform(get("/api/libraries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(library.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        // Get the library
        restLibraryMockMvc.perform(get("/api/libraries/{id}", library.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(library.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingLibrary() throws Exception {
        // Get the library
        restLibraryMockMvc.perform(get("/api/libraries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        int databaseSizeBeforeUpdate = libraryRepository.findAll().size();

        // Update the library
        Library updatedLibrary = libraryRepository.findById(library.getId()).get();
        // Disconnect from session so that the updates on updatedLibrary are not directly saved in db
        em.detach(updatedLibrary);
        LibraryDTO libraryDTO = libraryMapper.toDto(updatedLibrary);

        restLibraryMockMvc.perform(put("/api/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
            .andExpect(status().isOk());

        // Validate the Library in the database
        List<Library> libraryList = libraryRepository.findAll();
        assertThat(libraryList).hasSize(databaseSizeBeforeUpdate);
        Library testLibrary = libraryList.get(libraryList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingLibrary() throws Exception {
        int databaseSizeBeforeUpdate = libraryRepository.findAll().size();

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibraryMockMvc.perform(put("/api/libraries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        List<Library> libraryList = libraryRepository.findAll();
        assertThat(libraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        int databaseSizeBeforeDelete = libraryRepository.findAll().size();

        // Delete the library
        restLibraryMockMvc.perform(delete("/api/libraries/{id}", library.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Library> libraryList = libraryRepository.findAll();
        assertThat(libraryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
