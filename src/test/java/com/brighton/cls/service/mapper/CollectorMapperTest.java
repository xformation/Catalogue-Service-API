package com.brighton.cls.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CollectorMapperTest {

    private CollectorMapper collectorMapper;

    @BeforeEach
    public void setUp() {
        collectorMapper = new CollectorMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(collectorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(collectorMapper.fromId(null)).isNull();
    }
}
