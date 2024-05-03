package net.bookstore.service.entity;


import net.bookstore.converter.transferObj.GenreRequest;
import net.bookstore.entity.Genre;

public interface GenreService extends EntityReadService<Genre> {
    Genre createNewGenre(GenreRequest genreRequest);

    void deleteGenreById(Long id);

}
