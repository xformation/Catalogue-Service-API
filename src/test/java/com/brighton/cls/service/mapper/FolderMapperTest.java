package com.brighton.cls.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FolderMapperTest {

    private FolderMapper folderMapper;

    @BeforeEach
    public void setUp() {
        folderMapper = new FolderMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(folderMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(folderMapper.fromId(null)).isNull();
    }
}
