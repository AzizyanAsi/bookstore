package net.bookstore.converter.impl;

import lombok.RequiredArgsConstructor;
import net.bookstore.converter.IAuthorConverter;
import net.bookstore.converter.transferObj.AuthorRequest;
import net.bookstore.entity.Author;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorConverter implements IAuthorConverter {
    @Override
    public Author convertToEntity(AuthorRequest authorRequest) {
        return Optional.ofNullable(authorRequest)
                .map(req -> {
                    Author entity = new Author();
                    entity.setFirstName(req.getFirstName());
                    entity.setLastName(req.getLastName());
                    return entity;
                })
                .orElse(null);
    }
}
