package com.brighton.cls.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.brighton.cls.domain.Folder} entity.
 */
public class FolderDTO implements Serializable {
    
    private Long id;

    private String title;

    private Long parentId;

    private Boolean isOpened;

    private Boolean isChecked;

    private Boolean isFolder;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean isIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Boolean isOpened) {
        this.isOpened = isOpened;
    }

    public Boolean isIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Boolean isIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Boolean isFolder) {
        this.isFolder = isFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FolderDTO)) {
            return false;
        }

        return id != null && id.equals(((FolderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FolderDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", parentId=" + getParentId() +
            ", isOpened='" + isIsOpened() + "'" +
            ", isChecked='" + isIsChecked() + "'" +
            ", isFolder='" + isIsFolder() + "'" +
            "}";
    }
}
