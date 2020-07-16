package com.brighton.cls.service.mapper;


import com.brighton.cls.domain.*;
import com.brighton.cls.service.dto.LibraryFolderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link LibraryFolder} and its DTO {@link LibraryFolderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LibraryFolderMapper extends EntityMapper<LibraryFolderDTO, LibraryFolder> {



    default LibraryFolder fromId(Long id) {
        if (id == null) {
            return null;
        }
        LibraryFolder libraryFolder = new LibraryFolder();
        libraryFolder.setId(id);
        return libraryFolder;
    }
}
