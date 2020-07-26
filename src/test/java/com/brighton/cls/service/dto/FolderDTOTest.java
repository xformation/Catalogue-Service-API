package com.brighton.cls.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brighton.cls.web.rest.TestUtil;

public class FolderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FolderDTO.class);
        FolderDTO folderDTO1 = new FolderDTO();
        folderDTO1.setId(1L);
        FolderDTO folderDTO2 = new FolderDTO();
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
        folderDTO2.setId(folderDTO1.getId());
        assertThat(folderDTO1).isEqualTo(folderDTO2);
        folderDTO2.setId(2L);
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
        folderDTO1.setId(null);
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
    }
}
