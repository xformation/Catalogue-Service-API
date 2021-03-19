package com.brighton.cls.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.brighton.cls.domain.Dashboard} entity.
 */
public class DashboardDTO implements Serializable {
    
    private Long id;

    private String name;

    @Lob
    private byte[] dashboard;

    private String dashboardContentType;
    @Size(max = 5000)
    private String description;

    private String isMonitored;

    private String type;

    private String createdBy;

    private Instant createdOn;

    private String updatedBy;

    private Instant updatedOn;


    private Long collectorId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getDashboard() {
        return dashboard;
    }

    public void setDashboard(byte[] dashboard) {
        this.dashboard = dashboard;
    }

    public String getDashboardContentType() {
        return dashboardContentType;
    }

    public void setDashboardContentType(String dashboardContentType) {
        this.dashboardContentType = dashboardContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsMonitored() {
        return isMonitored;
    }

    public void setIsMonitored(String isMonitored) {
        this.isMonitored = isMonitored;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Long collectorId) {
        this.collectorId = collectorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DashboardDTO)) {
            return false;
        }

        return id != null && id.equals(((DashboardDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DashboardDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dashboard='" + getDashboard() + "'" +
            ", description='" + getDescription() + "'" +
            ", isMonitored='" + getIsMonitored() + "'" +
            ", type='" + getType() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", collectorId=" + getCollectorId() +
            "}";
    }
}
