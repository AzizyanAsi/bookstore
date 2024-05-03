package net.bookstore.service.entity.impl;


import lombok.extern.slf4j.Slf4j;
import net.bookstore.exception.common.EntityNotFoundException;
import net.bookstore.service.entity.BookService;
import net.bookstore.converter.IBookConverter;
import net.bookstore.converter.transferObj.BookRequest;
import net.bookstore.converter.transferObj.BookUpdateRequest;
import net.bookstore.entity.Book;
import net.bookstore.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static net.bookstore.common.cache.EntityCacheNames.ALL_BOOKS;
import static net.bookstore.common.cache.EntityCacheNames.BOOK;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final IBookConverter bookConverter;

    public BookServiceImpl(BookRepository bookRepository, IBookConverter bookConverter) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Book createNewBook(BookRequest bookRequest) {
        var savedBook = bookRepository.save(bookConverter.convertToEntity(bookRequest));
        log.info("Book successfully created: {}", savedBook);
        return savedBook;
    }

    @Override
    @Cacheable(value = ALL_BOOKS)
    public List<Book> getAllEntities() {
        return bookRepository.findAll();
    }

    @Override
    @Cacheable(value = BOOK, key = "#id", unless = "#result == null")
    public Book getEntity(Long id) {
        log.debug("requested to get the book with  id: {}", id);
        return this.bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Book with id {%d} not found", id)));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = BOOK, key = "#id"),
            @CacheEvict(value = ALL_BOOKS, allEntries = true)
    })
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void deleteBookById(Long id) {
        this.getEntity(id);
        bookRepository.deleteById(id);
        log.info("Book successfully deleted: {id = {}}", id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    @CacheEvict(value = BOOK, key = "#id")
    @CachePut(value = BOOK, key = "#id")
    public Book updateBookInfo(Long id, BookUpdateRequest bookRequest) {
        var converted = bookConverter.convertToEntityUpdateReq(bookRequest);
        Book loadedBook = this.getEntity(id);
        loadedBook.copyFieldsFrom(converted);
        log.info("Book successfully updated: {}", loadedBook);
        return bookRepository.save(loadedBook);
    }

    @Override
    public Page<Book> getAllBooksByFilter(String genre, String title, String author, Pageable pageable) {
        if ((title != null && !title.isEmpty()) || (author != null && !author.isEmpty())
                || (genre != null && !genre.isEmpty())) {
            return bookRepository.findAllBy(author, genre, title, pageable);
        } else {
            return this.getAllBooks(pageable);
        }
    }

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }


}
