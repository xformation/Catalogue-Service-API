package com.brighton.cls.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.brighton.cls.domain.ManageView} entity.
 */
public class ManageViewDTO implements Serializable {
    
    private Long id;

    private String name;

    @Lob
    private byte[] viewData;

    private String viewDataContentType;
    @Size(max = 5000)
    private String description;

    private String type;

    private String status;

    private String createdBy;

    private Instant createdOn;

    private String updatedBy;

    private Instant updatedOn;

    
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

    public byte[] getViewData() {
        return viewData;
    }

    public void setViewData(byte[] viewData) {
        this.viewData = viewData;
    }

    public String getViewDataContentType() {
        return viewDataContentType;
    }

    public void setViewDataContentType(String viewDataContentType) {
        this.viewDataContentType = viewDataContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManageViewDTO)) {
            return false;
        }

        return id != null && id.equals(((ManageViewDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManageViewDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", viewData='" + getViewData() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
