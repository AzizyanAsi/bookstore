package net.bookstore.service.entity;

import net.bookstore.converter.transferObj.AuthorRequest;
import net.bookstore.entity.Author;

public interface AuthorService extends EntityReadService<Author> {
    Author createNewAuthor(AuthorRequest authorRequest);

    void deleteAuthorById(Long id);

}
