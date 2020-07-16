package com.brighton.cls.repository;

import com.brighton.cls.domain.LibraryFolder;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LibraryFolder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LibraryFolderRepository extends JpaRepository<LibraryFolder, Long> {
}
