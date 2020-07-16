package com.brighton.cls.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.brighton.cls.domain.Collector} entity.
 */
public class CollectorDTO implements Serializable {
    
    private Long id;

    private String name;

    private String type;

    private String datasource;

    
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectorDTO)) {
            return false;
        }

        return id != null && id.equals(((CollectorDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", datasource='" + getDatasource() + "'" +
            "}";
    }
}
