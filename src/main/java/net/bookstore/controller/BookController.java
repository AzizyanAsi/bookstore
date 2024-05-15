package net.bookstore.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import net.bookstore.converter.transferObj.BookRequest;
import net.bookstore.converter.transferObj.BookResponse;
import net.bookstore.converter.transferObj.BookUpdateRequest;
import net.bookstore.entity.Book;
import net.bookstore.service.entity.BookService;
import net.bookstore.common.api.ApiResponse;
import net.bookstore.common.util.LocaleUtils;
import net.bookstore.converter.impl.ResponseMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("system/book")
public class BookController {
    private final BookService bookService;
    private final LocaleUtils localeUtils;
    private ResponseMappers responseMappers;

    public BookController(BookService bookService,
                          LocaleUtils localeUtils) {
        this.bookService = bookService;
        this.localeUtils = localeUtils;
    }

    @Autowired
    public void setResponseMappers(ResponseMappers responseMappers) {
        this.responseMappers = responseMappers;
    }

    @PostMapping("create")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookResponse> createNewBook(@Valid @RequestBody BookRequest bookRequest) {
        Book newBook = bookService.createNewBook(bookRequest);
        var responseUser = responseMappers.bookToResponse(newBook);
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.book.added"), responseUser);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.deleted"));
    }

    @GetMapping("{id}")
    public ApiResponse<BookResponse> searchBookById(
            @PathVariable("id") @Positive(message = "{validation.positive}") Long id) {
        var book = responseMappers.bookToResponse(bookService.getEntity(id));
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.found"), book);
    }


    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookResponse> updateBookInfo(
            @PathVariable("id") @Positive(message = "{validation.positive}") Long id,
            @Valid @RequestBody BookUpdateRequest bookRequest) {
        var bookResp = responseMappers.bookToResponse(
                bookService.updateBookInfo(id, bookRequest));
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.updated"), bookResp);
    }


    @GetMapping("list")
    public Page<BookResponse> searchFilteredBooks(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "title") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder, @RequestParam(required = false) String genre,
                                                  @RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(direction, sortBy));
        return bookService.getAllBooksByFilter(genre, title, author, pageable)
                .map(responseMappers::bookToResponse);
    }
}
