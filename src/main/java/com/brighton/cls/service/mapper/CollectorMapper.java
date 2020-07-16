package com.brighton.cls.service.mapper;


import com.brighton.cls.domain.*;
import com.brighton.cls.service.dto.CollectorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Collector} and its DTO {@link CollectorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CollectorMapper extends EntityMapper<CollectorDTO, Collector> {



    default Collector fromId(Long id) {
        if (id == null) {
            return null;
        }
        Collector collector = new Collector();
        collector.setId(id);
        return collector;
    }
}
