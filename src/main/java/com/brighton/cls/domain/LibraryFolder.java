package com.brighton.cls.domain;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A LibraryFolder.
 */
@Entity
@Table(name = "library_folder")
public class LibraryFolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "folder")
    private String folder;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public LibraryFolder folder(String folder) {
        this.folder = folder;
        return this;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibraryFolder)) {
            return false;
        }
        return id != null && id.equals(((LibraryFolder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibraryFolder{" +
            "id=" + getId() +
            ", folder='" + getFolder() + "'" +
            "}";
    }
}
