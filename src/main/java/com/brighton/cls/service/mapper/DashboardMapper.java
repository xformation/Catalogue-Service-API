package com.brighton.cls.service.mapper;


import com.brighton.cls.domain.*;
import com.brighton.cls.service.dto.DashboardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dashboard} and its DTO {@link DashboardDTO}.
 */
@Mapper(componentModel = "spring", uses = {CollectorMapper.class})
public interface DashboardMapper extends EntityMapper<DashboardDTO, Dashboard> {

    @Mapping(source = "collector.id", target = "collectorId")
    DashboardDTO toDto(Dashboard dashboard);

    @Mapping(source = "collectorId", target = "collector")
    Dashboard toEntity(DashboardDTO dashboardDTO);

    default Dashboard fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dashboard dashboard = new Dashboard();
        dashboard.setId(id);
        return dashboard;
    }
}
