package com.brighton.cls.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.brighton.cls.domain.Dashboard} entity.
 */
public class DashboardDTO implements Serializable {
    
    private Long id;

    @Lob
    private byte[] dashboard;

    private String dashboardContentType;
    @Size(max = 5000)
    private String documentation;


    private Long collectorId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
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
            ", dashboard='" + getDashboard() + "'" +
            ", documentation='" + getDocumentation() + "'" +
            ", collectorId=" + getCollectorId() +
            "}";
    }
}
