package net.bookstore.service.entity.impl;

import lombok.extern.slf4j.Slf4j;
import net.bookstore.exception.common.EntityNotFoundException;
import net.bookstore.converter.IAuthorConverter;
import net.bookstore.converter.transferObj.AuthorRequest;
import net.bookstore.entity.Author;
import net.bookstore.repository.AuthorRepository;
import net.bookstore.service.entity.AuthorService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.bookstore.common.cache.EntityCacheNames.ALL_AUTHORS;
import static net.bookstore.common.cache.EntityCacheNames.AUTHOR;


@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final IAuthorConverter authorConverter;

    public AuthorServiceImpl(AuthorRepository authorRepository, IAuthorConverter authorConverter) {
        this.authorRepository = authorRepository;
        this.authorConverter = authorConverter;
    }

    @Override
    public Author createNewAuthor(AuthorRequest authorRequest) {
        var savedAuthor = authorRepository.save(authorConverter.convertToEntity(authorRequest));
        log.info("Author successfully created: {}", savedAuthor);
        return savedAuthor;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = AUTHOR, key = "#id"),
            @CacheEvict(value = ALL_AUTHORS, allEntries = true)
    })
    public void deleteAuthorById(Long id) {
        this.getEntity(id);
        authorRepository.deleteById(id);
        log.info("Author successfully deleted: {id = {}}", id);
    }

    @Override
    @Cacheable(value = ALL_AUTHORS)
    public List<Author> getAllEntities() {
        return authorRepository.findAll();
    }

    @Override
    @Cacheable(value = AUTHOR, key = "#id", unless = "#result == null")
    public Author getEntity(Long id) {
        log.debug("requested to get the author with  id: {}", id);
        return this.authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Author with id {%d} not found", id)));
    }

}
