package com.bookify.api;
import com.bookify.api.model.book.BookRequest;
import com.bookify.api.model.book.BookResponse;

import java.util.List;


public interface BookService {

   List<BookResponse> getAllBooks();
   BookResponse createBook(BookRequest bookRequest);
}
