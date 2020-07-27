package com.brighton.cls.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.brighton.cls.domain.Library} entity.
 */
public class LibraryDTO implements Serializable {
    
    private Long id;

    private String appName;

    @Size(max = 2000)
    private String virtualPath;

    private String dataSource;


    private Long collectorId;

    private Long folderId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVirtualPath() {
        return virtualPath;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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
            ", appName='" + getAppName() + "'" +
            ", virtualPath='" + getVirtualPath() + "'" +
            ", dataSource='" + getDataSource() + "'" +
            ", collectorId=" + getCollectorId() +
            ", folderId=" + getFolderId() +
            "}";
    }
}
