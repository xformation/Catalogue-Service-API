package com.brighton.cls.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Dashboard.
 */
@Entity
@Table(name = "dashboard")
public class Dashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "dashboard")
    private byte[] dashboard;

    @Column(name = "dashboard_content_type")
    private String dashboardContentType;

    @Size(max = 5000)
    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "is_monitored")
    private String isMonitored;

    @Column(name = "type")
    private String type;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @ManyToOne
    @JsonIgnoreProperties(value = "dashboards", allowSetters = true)
    private Collector collector;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Dashboard name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getDashboard() {
        return dashboard;
    }

    public Dashboard dashboard(byte[] dashboard) {
        this.dashboard = dashboard;
        return this;
    }

    public void setDashboard(byte[] dashboard) {
        this.dashboard = dashboard;
    }

    public String getDashboardContentType() {
        return dashboardContentType;
    }

    public Dashboard dashboardContentType(String dashboardContentType) {
        this.dashboardContentType = dashboardContentType;
        return this;
    }

    public void setDashboardContentType(String dashboardContentType) {
        this.dashboardContentType = dashboardContentType;
    }

    public String getDescription() {
        return description;
    }

    public Dashboard description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsMonitored() {
        return isMonitored;
    }

    public Dashboard isMonitored(String isMonitored) {
        this.isMonitored = isMonitored;
        return this;
    }

    public void setIsMonitored(String isMonitored) {
        this.isMonitored = isMonitored;
    }

    public String getType() {
        return type;
    }

    public Dashboard type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Dashboard createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Dashboard createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Dashboard updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public Dashboard updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Collector getCollector() {
        return collector;
    }

    public Dashboard collector(Collector collector) {
        this.collector = collector;
        return this;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dashboard)) {
            return false;
        }
        return id != null && id.equals(((Dashboard) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dashboard{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dashboard='" + getDashboard() + "'" +
            ", dashboardContentType='" + getDashboardContentType() + "'" +
            ", description='" + getDescription() + "'" +
            ", isMonitored='" + getIsMonitored() + "'" +
            ", type='" + getType() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
