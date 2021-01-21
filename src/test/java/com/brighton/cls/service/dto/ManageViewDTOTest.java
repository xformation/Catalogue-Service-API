package com.brighton.cls.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brighton.cls.web.rest.TestUtil;

public class ManageViewDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManageViewDTO.class);
        ManageViewDTO manageViewDTO1 = new ManageViewDTO();
        manageViewDTO1.setId(1L);
        ManageViewDTO manageViewDTO2 = new ManageViewDTO();
        assertThat(manageViewDTO1).isNotEqualTo(manageViewDTO2);
        manageViewDTO2.setId(manageViewDTO1.getId());
        assertThat(manageViewDTO1).isEqualTo(manageViewDTO2);
        manageViewDTO2.setId(2L);
        assertThat(manageViewDTO1).isNotEqualTo(manageViewDTO2);
        manageViewDTO1.setId(null);
        assertThat(manageViewDTO1).isNotEqualTo(manageViewDTO2);
    }
}
