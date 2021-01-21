package com.brighton.cls.web.rest;

import com.brighton.cls.CatalogueserviceApp;
import com.brighton.cls.domain.ManageView;
import com.brighton.cls.repository.ManageViewRepository;
import com.brighton.cls.service.ManageViewService;
import com.brighton.cls.service.dto.ManageViewDTO;
import com.brighton.cls.service.mapper.ManageViewMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ManageViewResource} REST controller.
 */
@SpringBootTest(classes = CatalogueserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ManageViewResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_VIEW_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIEW_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VIEW_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIEW_DATA_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ManageViewRepository manageViewRepository;

    @Autowired
    private ManageViewMapper manageViewMapper;

    @Autowired
    private ManageViewService manageViewService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManageViewMockMvc;

    private ManageView manageView;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManageView createEntity(EntityManager em) {
        ManageView manageView = new ManageView()
            .name(DEFAULT_NAME)
            .viewData(DEFAULT_VIEW_DATA)
            .viewDataContentType(DEFAULT_VIEW_DATA_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
        return manageView;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManageView createUpdatedEntity(EntityManager em) {
        ManageView manageView = new ManageView()
            .name(UPDATED_NAME)
            .viewData(UPDATED_VIEW_DATA)
            .viewDataContentType(UPDATED_VIEW_DATA_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
        return manageView;
    }

    @BeforeEach
    public void initTest() {
        manageView = createEntity(em);
    }

    @Test
    @Transactional
    public void createManageView() throws Exception {
        int databaseSizeBeforeCreate = manageViewRepository.findAll().size();
        // Create the ManageView
        ManageViewDTO manageViewDTO = manageViewMapper.toDto(manageView);
        restManageViewMockMvc.perform(post("/api/manage-views")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manageViewDTO)))
            .andExpect(status().isCreated());

        // Validate the ManageView in the database
        List<ManageView> manageViewList = manageViewRepository.findAll();
        assertThat(manageViewList).hasSize(databaseSizeBeforeCreate + 1);
        ManageView testManageView = manageViewList.get(manageViewList.size() - 1);
        assertThat(testManageView.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testManageView.getViewData()).isEqualTo(DEFAULT_VIEW_DATA);
        assertThat(testManageView.getViewDataContentType()).isEqualTo(DEFAULT_VIEW_DATA_CONTENT_TYPE);
        assertThat(testManageView.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testManageView.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testManageView.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testManageView.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testManageView.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testManageView.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testManageView.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    public void createManageViewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = manageViewRepository.findAll().size();

        // Create the ManageView with an existing ID
        manageView.setId(1L);
        ManageViewDTO manageViewDTO = manageViewMapper.toDto(manageView);

        // An entity with an existing ID cannot be created, so this API call must fail
        restManageViewMockMvc.perform(post("/api/manage-views")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manageViewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ManageView in the database
        List<ManageView> manageViewList = manageViewRepository.findAll();
        assertThat(manageViewList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllManageViews() throws Exception {
        // Initialize the database
        manageViewRepository.saveAndFlush(manageView);

        // Get all the manageViewList
        restManageViewMockMvc.perform(get("/api/manage-views?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manageView.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].viewDataContentType").value(hasItem(DEFAULT_VIEW_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].viewData").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIEW_DATA))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getManageView() throws Exception {
        // Initialize the database
        manageViewRepository.saveAndFlush(manageView);

        // Get the manageView
        restManageViewMockMvc.perform(get("/api/manage-views/{id}", manageView.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manageView.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.viewDataContentType").value(DEFAULT_VIEW_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.viewData").value(Base64Utils.encodeToString(DEFAULT_VIEW_DATA)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingManageView() throws Exception {
        // Get the manageView
        restManageViewMockMvc.perform(get("/api/manage-views/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManageView() throws Exception {
        // Initialize the database
        manageViewRepository.saveAndFlush(manageView);

        int databaseSizeBeforeUpdate = manageViewRepository.findAll().size();

        // Update the manageView
        ManageView updatedManageView = manageViewRepository.findById(manageView.getId()).get();
        // Disconnect from session so that the updates on updatedManageView are not directly saved in db
        em.detach(updatedManageView);
        updatedManageView
            .name(UPDATED_NAME)
            .viewData(UPDATED_VIEW_DATA)
            .viewDataContentType(UPDATED_VIEW_DATA_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
        ManageViewDTO manageViewDTO = manageViewMapper.toDto(updatedManageView);

        restManageViewMockMvc.perform(put("/api/manage-views")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manageViewDTO)))
            .andExpect(status().isOk());

        // Validate the ManageView in the database
        List<ManageView> manageViewList = manageViewRepository.findAll();
        assertThat(manageViewList).hasSize(databaseSizeBeforeUpdate);
        ManageView testManageView = manageViewList.get(manageViewList.size() - 1);
        assertThat(testManageView.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManageView.getViewData()).isEqualTo(UPDATED_VIEW_DATA);
        assertThat(testManageView.getViewDataContentType()).isEqualTo(UPDATED_VIEW_DATA_CONTENT_TYPE);
        assertThat(testManageView.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testManageView.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testManageView.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testManageView.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testManageView.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testManageView.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testManageView.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingManageView() throws Exception {
        int databaseSizeBeforeUpdate = manageViewRepository.findAll().size();

        // Create the ManageView
        ManageViewDTO manageViewDTO = manageViewMapper.toDto(manageView);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManageViewMockMvc.perform(put("/api/manage-views")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manageViewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ManageView in the database
        List<ManageView> manageViewList = manageViewRepository.findAll();
        assertThat(manageViewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteManageView() throws Exception {
        // Initialize the database
        manageViewRepository.saveAndFlush(manageView);

        int databaseSizeBeforeDelete = manageViewRepository.findAll().size();

        // Delete the manageView
        restManageViewMockMvc.perform(delete("/api/manage-views/{id}", manageView.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ManageView> manageViewList = manageViewRepository.findAll();
        assertThat(manageViewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
