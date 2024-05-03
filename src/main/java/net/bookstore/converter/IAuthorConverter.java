package net.bookstore.converter;

import net.bookstore.entity.Author;
import net.bookstore.converter.transferObj.AuthorRequest;

public interface IAuthorConverter {
    Author convertToEntity(AuthorRequest authorRequest);
}
