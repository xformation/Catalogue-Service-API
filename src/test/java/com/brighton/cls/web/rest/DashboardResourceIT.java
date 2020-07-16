package com.brighton.cls.web.rest;

import com.brighton.cls.CatalogueserviceApp;
import com.brighton.cls.domain.Dashboard;
import com.brighton.cls.repository.DashboardRepository;
import com.brighton.cls.service.DashboardService;
import com.brighton.cls.service.dto.DashboardDTO;
import com.brighton.cls.service.mapper.DashboardMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@SpringBootTest(classes = CatalogueserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DashboardResourceIT {

    private static final byte[] DEFAULT_DASHBOARD = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DASHBOARD = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DASHBOARD_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DASHBOARD_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DOCUMENTATION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION = "BBBBBBBBBB";

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private DashboardMapper dashboardMapper;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDashboardMockMvc;

    private Dashboard dashboard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createEntity(EntityManager em) {
        Dashboard dashboard = new Dashboard()
            .dashboard(DEFAULT_DASHBOARD)
            .dashboardContentType(DEFAULT_DASHBOARD_CONTENT_TYPE)
            .documentation(DEFAULT_DOCUMENTATION);
        return dashboard;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createUpdatedEntity(EntityManager em) {
        Dashboard dashboard = new Dashboard()
            .dashboard(UPDATED_DASHBOARD)
            .dashboardContentType(UPDATED_DASHBOARD_CONTENT_TYPE)
            .documentation(UPDATED_DOCUMENTATION);
        return dashboard;
    }

    @BeforeEach
    public void initTest() {
        dashboard = createEntity(em);
    }

    @Test
    @Transactional
    public void createDashboard() throws Exception {
        int databaseSizeBeforeCreate = dashboardRepository.findAll().size();
        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);
        restDashboardMockMvc.perform(post("/api/dashboards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dashboardDTO)))
            .andExpect(status().isCreated());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeCreate + 1);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getDashboard()).isEqualTo(DEFAULT_DASHBOARD);
        assertThat(testDashboard.getDashboardContentType()).isEqualTo(DEFAULT_DASHBOARD_CONTENT_TYPE);
        assertThat(testDashboard.getDocumentation()).isEqualTo(DEFAULT_DOCUMENTATION);
    }

    @Test
    @Transactional
    public void createDashboardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dashboardRepository.findAll().size();

        // Create the Dashboard with an existing ID
        dashboard.setId(1L);
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDashboardMockMvc.perform(post("/api/dashboards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dashboardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDashboards() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList
        restDashboardMockMvc.perform(get("/api/dashboards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dashboard.getId().intValue())))
            .andExpect(jsonPath("$.[*].dashboardContentType").value(hasItem(DEFAULT_DASHBOARD_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dashboard").value(hasItem(Base64Utils.encodeToString(DEFAULT_DASHBOARD))))
            .andExpect(jsonPath("$.[*].documentation").value(hasItem(DEFAULT_DOCUMENTATION)));
    }
    
    @Test
    @Transactional
    public void getDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        // Get the dashboard
        restDashboardMockMvc.perform(get("/api/dashboards/{id}", dashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dashboard.getId().intValue()))
            .andExpect(jsonPath("$.dashboardContentType").value(DEFAULT_DASHBOARD_CONTENT_TYPE))
            .andExpect(jsonPath("$.dashboard").value(Base64Utils.encodeToString(DEFAULT_DASHBOARD)))
            .andExpect(jsonPath("$.documentation").value(DEFAULT_DOCUMENTATION));
    }
    @Test
    @Transactional
    public void getNonExistingDashboard() throws Exception {
        // Get the dashboard
        restDashboardMockMvc.perform(get("/api/dashboards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Update the dashboard
        Dashboard updatedDashboard = dashboardRepository.findById(dashboard.getId()).get();
        // Disconnect from session so that the updates on updatedDashboard are not directly saved in db
        em.detach(updatedDashboard);
        updatedDashboard
            .dashboard(UPDATED_DASHBOARD)
            .dashboardContentType(UPDATED_DASHBOARD_CONTENT_TYPE)
            .documentation(UPDATED_DOCUMENTATION);
        DashboardDTO dashboardDTO = dashboardMapper.toDto(updatedDashboard);

        restDashboardMockMvc.perform(put("/api/dashboards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dashboardDTO)))
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
        Dashboard testDashboard = dashboardList.get(dashboardList.size() - 1);
        assertThat(testDashboard.getDashboard()).isEqualTo(UPDATED_DASHBOARD);
        assertThat(testDashboard.getDashboardContentType()).isEqualTo(UPDATED_DASHBOARD_CONTENT_TYPE);
        assertThat(testDashboard.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
    }

    @Test
    @Transactional
    public void updateNonExistingDashboard() throws Exception {
        int databaseSizeBeforeUpdate = dashboardRepository.findAll().size();

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc.perform(put("/api/dashboards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dashboardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDashboard() throws Exception {
        // Initialize the database
        dashboardRepository.saveAndFlush(dashboard);

        int databaseSizeBeforeDelete = dashboardRepository.findAll().size();

        // Delete the dashboard
        restDashboardMockMvc.perform(delete("/api/dashboards/{id}", dashboard.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dashboard> dashboardList = dashboardRepository.findAll();
        assertThat(dashboardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
