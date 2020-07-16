package com.brighton.cls.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brighton.cls.web.rest.TestUtil;

public class LibraryFolderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LibraryFolderDTO.class);
        LibraryFolderDTO libraryFolderDTO1 = new LibraryFolderDTO();
        libraryFolderDTO1.setId(1L);
        LibraryFolderDTO libraryFolderDTO2 = new LibraryFolderDTO();
        assertThat(libraryFolderDTO1).isNotEqualTo(libraryFolderDTO2);
        libraryFolderDTO2.setId(libraryFolderDTO1.getId());
        assertThat(libraryFolderDTO1).isEqualTo(libraryFolderDTO2);
        libraryFolderDTO2.setId(2L);
        assertThat(libraryFolderDTO1).isNotEqualTo(libraryFolderDTO2);
        libraryFolderDTO1.setId(null);
        assertThat(libraryFolderDTO1).isNotEqualTo(libraryFolderDTO2);
    }
}
