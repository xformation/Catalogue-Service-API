package com.brighton.cls.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Library.
 */
@Entity
@Table(name = "library")
public class Library implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = "libraries", allowSetters = true)
    private Collector collector;

    @ManyToOne
    @JsonIgnoreProperties(value = "libraries", allowSetters = true)
    private LibraryFolder libraryFolder;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collector getCollector() {
        return collector;
    }

    public Library collector(Collector collector) {
        this.collector = collector;
        return this;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public LibraryFolder getLibraryFolder() {
        return libraryFolder;
    }

    public Library libraryFolder(LibraryFolder libraryFolder) {
        this.libraryFolder = libraryFolder;
        return this;
    }

    public void setLibraryFolder(LibraryFolder libraryFolder) {
        this.libraryFolder = libraryFolder;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Library)) {
            return false;
        }
        return id != null && id.equals(((Library) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Library{" +
            "id=" + getId() +
            "}";
    }
}
