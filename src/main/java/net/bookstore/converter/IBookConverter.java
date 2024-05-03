package net.bookstore.converter;

import net.bookstore.converter.transferObj.BookRequest;
import net.bookstore.entity.Book;
import net.bookstore.converter.transferObj.BookUpdateRequest;


public interface IBookConverter {
    Book convertToEntity(BookRequest bookRequest);

    Book convertToEntityUpdateReq(BookUpdateRequest bookRequest);
}

