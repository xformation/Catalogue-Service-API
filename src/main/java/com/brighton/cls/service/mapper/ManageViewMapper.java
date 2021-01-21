package com.brighton.cls.service.mapper;


import com.brighton.cls.domain.*;
import com.brighton.cls.service.dto.ManageViewDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ManageView} and its DTO {@link ManageViewDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManageViewMapper extends EntityMapper<ManageViewDTO, ManageView> {



    default ManageView fromId(Long id) {
        if (id == null) {
            return null;
        }
        ManageView manageView = new ManageView();
        manageView.setId(id);
        return manageView;
    }
}
