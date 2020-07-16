package com.brighton.cls.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.brighton.cls.domain.LibraryFolder} entity.
 */
public class LibraryFolderDTO implements Serializable {
    
    private Long id;

    private String folder;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibraryFolderDTO)) {
            return false;
        }

        return id != null && id.equals(((LibraryFolderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibraryFolderDTO{" +
            "id=" + getId() +
            ", folder='" + getFolder() + "'" +
            "}";
    }
}
