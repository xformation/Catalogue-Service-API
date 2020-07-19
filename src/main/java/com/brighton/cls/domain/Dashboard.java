package com.brighton.cls.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

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
            "}";
    }
}
