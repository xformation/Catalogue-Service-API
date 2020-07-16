package com.brighton.cls.service.mapper;


import com.brighton.cls.domain.*;
import com.brighton.cls.service.dto.LibraryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Library} and its DTO {@link LibraryDTO}.
 */
@Mapper(componentModel = "spring", uses = {CollectorMapper.class, LibraryFolderMapper.class})
public interface LibraryMapper extends EntityMapper<LibraryDTO, Library> {

    @Mapping(source = "collector.id", target = "collectorId")
    @Mapping(source = "libraryFolder.id", target = "libraryFolderId")
    LibraryDTO toDto(Library library);

    @Mapping(source = "collectorId", target = "collector")
    @Mapping(source = "libraryFolderId", target = "libraryFolder")
    Library toEntity(LibraryDTO libraryDTO);

    default Library fromId(Long id) {
        if (id == null) {
            return null;
        }
        Library library = new Library();
        library.setId(id);
        return library;
    }
}
