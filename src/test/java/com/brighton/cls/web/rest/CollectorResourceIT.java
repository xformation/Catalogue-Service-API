package com.brighton.cls.web.rest;

import com.brighton.cls.CatalogueserviceApp;
import com.brighton.cls.domain.Collector;
import com.brighton.cls.repository.CollectorRepository;
import com.brighton.cls.service.CollectorService;
import com.brighton.cls.service.dto.CollectorDTO;
import com.brighton.cls.service.mapper.CollectorMapper;

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
 * Integration tests for the {@link CollectorResource} REST controller.
 */
@SpringBootTest(classes = CatalogueserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CollectorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATASOURCE = "AAAAAAAAAA";
    private static final String UPDATED_DATASOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CollectorRepository collectorRepository;

    @Autowired
    private CollectorMapper collectorMapper;

    @Autowired
    private CollectorService collectorService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollectorMockMvc;

    private Collector collector;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collector createEntity(EntityManager em) {
        Collector collector = new Collector()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .datasource(DEFAULT_DATASOURCE)
            .description(DEFAULT_DESCRIPTION);
        return collector;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collector createUpdatedEntity(EntityManager em) {
        Collector collector = new Collector()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .datasource(UPDATED_DATASOURCE)
            .description(UPDATED_DESCRIPTION);
        return collector;
    }

    @BeforeEach
    public void initTest() {
        collector = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollector() throws Exception {
        int databaseSizeBeforeCreate = collectorRepository.findAll().size();
        // Create the Collector
        CollectorDTO collectorDTO = collectorMapper.toDto(collector);
        restCollectorMockMvc.perform(post("/api/collectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectorDTO)))
            .andExpect(status().isCreated());

        // Validate the Collector in the database
        List<Collector> collectorList = collectorRepository.findAll();
        assertThat(collectorList).hasSize(databaseSizeBeforeCreate + 1);
        Collector testCollector = collectorList.get(collectorList.size() - 1);
        assertThat(testCollector.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollector.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCollector.getDatasource()).isEqualTo(DEFAULT_DATASOURCE);
        assertThat(testCollector.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCollectorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collectorRepository.findAll().size();

        // Create the Collector with an existing ID
        collector.setId(1L);
        CollectorDTO collectorDTO = collectorMapper.toDto(collector);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectorMockMvc.perform(post("/api/collectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collector in the database
        List<Collector> collectorList = collectorRepository.findAll();
        assertThat(collectorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCollectors() throws Exception {
        // Initialize the database
        collectorRepository.saveAndFlush(collector);

        // Get all the collectorList
        restCollectorMockMvc.perform(get("/api/collectors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].datasource").value(hasItem(DEFAULT_DATASOURCE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getCollector() throws Exception {
        // Initialize the database
        collectorRepository.saveAndFlush(collector);

        // Get the collector
        restCollectorMockMvc.perform(get("/api/collectors/{id}", collector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collector.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.datasource").value(DEFAULT_DATASOURCE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingCollector() throws Exception {
        // Get the collector
        restCollectorMockMvc.perform(get("/api/collectors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollector() throws Exception {
        // Initialize the database
        collectorRepository.saveAndFlush(collector);

        int databaseSizeBeforeUpdate = collectorRepository.findAll().size();

        // Update the collector
        Collector updatedCollector = collectorRepository.findById(collector.getId()).get();
        // Disconnect from session so that the updates on updatedCollector are not directly saved in db
        em.detach(updatedCollector);
        updatedCollector
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .datasource(UPDATED_DATASOURCE)
            .description(UPDATED_DESCRIPTION);
        CollectorDTO collectorDTO = collectorMapper.toDto(updatedCollector);

        restCollectorMockMvc.perform(put("/api/collectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectorDTO)))
            .andExpect(status().isOk());

        // Validate the Collector in the database
        List<Collector> collectorList = collectorRepository.findAll();
        assertThat(collectorList).hasSize(databaseSizeBeforeUpdate);
        Collector testCollector = collectorList.get(collectorList.size() - 1);
        assertThat(testCollector.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollector.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCollector.getDatasource()).isEqualTo(UPDATED_DATASOURCE);
        assertThat(testCollector.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCollector() throws Exception {
        int databaseSizeBeforeUpdate = collectorRepository.findAll().size();

        // Create the Collector
        CollectorDTO collectorDTO = collectorMapper.toDto(collector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectorMockMvc.perform(put("/api/collectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collector in the database
        List<Collector> collectorList = collectorRepository.findAll();
        assertThat(collectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCollector() throws Exception {
        // Initialize the database
        collectorRepository.saveAndFlush(collector);

        int databaseSizeBeforeDelete = collectorRepository.findAll().size();

        // Delete the collector
        restCollectorMockMvc.perform(delete("/api/collectors/{id}", collector.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Collector> collectorList = collectorRepository.findAll();
        assertThat(collectorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
