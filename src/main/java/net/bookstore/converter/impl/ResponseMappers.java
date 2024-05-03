package net.bookstore.converter.impl;

import net.bookstore.converter.transferObj.*;
import net.bookstore.entity.*;
import net.bookstore.common.config.StorageConfig;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class ResponseMappers {

    private StorageConfig storageConfig;

    @Autowired
    public void setStorageConfig(StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
    }

    public abstract RoleResponse roleToResponse(Role role);
    public abstract BookResponse bookToResponse(Book book);
    public abstract GenreResponse genreToResponse(Genre genre);
    public abstract AuthorResponse authorToResponse(Author author);




    @InheritConfiguration
    public abstract SystemUserResponse systemUserToResponse(SystemUser systemUser);


}
