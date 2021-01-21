package com.brighton.cls.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ManageViewMapperTest {

    private ManageViewMapper manageViewMapper;

//    @BeforeEach
//    public void setUp() {
//        manageViewMapper = new ManageViewMapperImpl();
//    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(manageViewMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(manageViewMapper.fromId(null)).isNull();
    }
}
