package com.brighton.cls.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brighton.cls.web.rest.TestUtil;

public class CollectorDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectorDTO.class);
        CollectorDTO collectorDTO1 = new CollectorDTO();
        collectorDTO1.setId(1L);
        CollectorDTO collectorDTO2 = new CollectorDTO();
        assertThat(collectorDTO1).isNotEqualTo(collectorDTO2);
        collectorDTO2.setId(collectorDTO1.getId());
        assertThat(collectorDTO1).isEqualTo(collectorDTO2);
        collectorDTO2.setId(2L);
        assertThat(collectorDTO1).isNotEqualTo(collectorDTO2);
        collectorDTO1.setId(null);
        assertThat(collectorDTO1).isNotEqualTo(collectorDTO2);
    }
}
