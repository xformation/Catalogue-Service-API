package com.brighton.cls.service.mapper;


import com.brighton.cls.domain.*;
import com.brighton.cls.service.dto.LibraryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Library} and its DTO {@link LibraryDTO}.
 */
@Mapper(componentModel = "spring", uses = {CollectorMapper.class, FolderMapper.class})
public interface LibraryMapper extends EntityMapper<LibraryDTO, Library> {

    @Mapping(source = "collector.id", target = "collectorId")
    @Mapping(source = "folder.id", target = "folderId")
    LibraryDTO toDto(Library library);

    @Mapping(source = "collectorId", target = "collector")
    @Mapping(source = "folderId", target = "folder")
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
