package com.brighton.cls.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brighton.cls.web.rest.TestUtil;

public class LibraryFolderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LibraryFolder.class);
        LibraryFolder libraryFolder1 = new LibraryFolder();
        libraryFolder1.setId(1L);
        LibraryFolder libraryFolder2 = new LibraryFolder();
        libraryFolder2.setId(libraryFolder1.getId());
        assertThat(libraryFolder1).isEqualTo(libraryFolder2);
        libraryFolder2.setId(2L);
        assertThat(libraryFolder1).isNotEqualTo(libraryFolder2);
        libraryFolder1.setId(null);
        assertThat(libraryFolder1).isNotEqualTo(libraryFolder2);
    }
}
