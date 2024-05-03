package net.bookstore.converter.impl;



import lombok.RequiredArgsConstructor;
import net.bookstore.converter.IBookConverter;
import net.bookstore.converter.transferObj.BookRequest;
import net.bookstore.entity.Book;
import net.bookstore.service.entity.AuthorService;
import net.bookstore.service.entity.GenreService;
import net.bookstore.converter.transferObj.BookUpdateRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookConverter  implements IBookConverter {

    private final GenreService genreService;
    private final AuthorService authorService;


    @Override
    public Book convertToEntity(BookRequest bookRequest) {
        return Optional.ofNullable(bookRequest)
                .map(req -> {
                    Book entity = new Book();
                    entity.setTitle(req.getTitle());
                    entity.setPrice(req.getPrice());
                    entity.setGenre(genreService.getEntity(req.getGenreId()));
                    entity.setAuthor(authorService.getEntity(req.getAuthorId()));
                    return entity;
                })
                .orElse(null);
    }

    @Override
    public Book convertToEntityUpdateReq(BookUpdateRequest bookRequest) {
        return Optional.ofNullable(bookRequest)
                .map(req -> {
                    Book entity = new Book();
                    entity.setTitle(req.getTitle());
                    entity.setPrice(req.getPrice());
                    return entity;
                })
                .orElse(null);
    }
}

