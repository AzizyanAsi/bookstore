package net.bookstore.service.entity.impl;


import lombok.extern.slf4j.Slf4j;
import net.bookstore.exception.common.EntityNotFoundException;
import net.bookstore.converter.transferObj.GenreRequest;
import net.bookstore.entity.Genre;
import net.bookstore.repository.GenreRepository;
import net.bookstore.service.entity.GenreService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.bookstore.common.cache.EntityCacheNames.ALL_GENRES;
import static net.bookstore.common.cache.EntityCacheNames.GENRE;


@Slf4j
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Cacheable(value = ALL_GENRES)
    public List<Genre> getAllEntities() {
        return genreRepository.findAll();
    }

    @Override
    @Cacheable(value = GENRE, key = "#id", unless = "#result == null")
    public Genre getEntity(Long id) {
        log.debug("requested to get the genre with  id: {}", id);
        return this.genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Genre with id {%d} not found", id)));
    }

    @Override
    public Genre createNewGenre(GenreRequest genreRequest) {
        Genre newGenre = new Genre();
        newGenre.setName(genreRequest.getGenreName());
        var savedGenre = genreRepository.save(newGenre);
        log.info("Genre successfully created: {}", savedGenre);
        return savedGenre;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = GENRE, key = "#id"),
            @CacheEvict(value = ALL_GENRES, allEntries = true)
    })
    public void deleteGenreById(Long id) {
        this.getEntity(id);
        genreRepository.deleteById(id);
        log.info("Genre successfully deleted: {id = {}}", id);
    }
}
