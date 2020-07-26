package com.brighton.cls.service.mapper;


import com.brighton.cls.domain.*;
import com.brighton.cls.service.dto.FolderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Folder} and its DTO {@link FolderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FolderMapper extends EntityMapper<FolderDTO, Folder> {



    default Folder fromId(Long id) {
        if (id == null) {
            return null;
        }
        Folder folder = new Folder();
        folder.setId(id);
        return folder;
    }
}
