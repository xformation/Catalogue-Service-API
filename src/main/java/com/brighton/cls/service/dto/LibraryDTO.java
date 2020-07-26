package com.brighton.cls.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.brighton.cls.domain.Library} entity.
 */
public class LibraryDTO implements Serializable {
    
    private Long id;


    private Long collectorId;

    private Long folderId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Long collectorId) {
        this.collectorId = collectorId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibraryDTO)) {
            return false;
        }

        return id != null && id.equals(((LibraryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibraryDTO{" +
            "id=" + getId() +
            ", collectorId=" + getCollectorId() +
            ", folderId=" + getFolderId() +
            "}";
    }
}
