package net.bookstore.service.entity;

import net.bookstore.converter.transferObj.BookRequest;
import net.bookstore.converter.transferObj.BookUpdateRequest;
import net.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService extends EntityReadService<Book> {
    Book createNewBook(BookRequest bookRequest);

    void deleteBookById(Long id);

    Book updateBookInfo(Long id, BookUpdateRequest bookRequest);

    Page<Book> getAllBooksByFilter(String genre, String title, String author, Pageable pageable);

    Page<Book> getAllBooks(Pageable pageable);
}
