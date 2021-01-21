package com.brighton.cls.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.brighton.cls.web.rest.TestUtil;

public class ManageViewTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManageView.class);
        ManageView manageView1 = new ManageView();
        manageView1.setId(1L);
        ManageView manageView2 = new ManageView();
        manageView2.setId(manageView1.getId());
        assertThat(manageView1).isEqualTo(manageView2);
        manageView2.setId(2L);
        assertThat(manageView1).isNotEqualTo(manageView2);
        manageView1.setId(null);
        assertThat(manageView1).isNotEqualTo(manageView2);
    }
}
