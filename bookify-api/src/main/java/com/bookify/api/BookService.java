package com.bookify.api;

import com.bookify.api.model.book.BookResponse;
import com.bookify.api.model.exception.ApiException;

import java.util.List;


public interface BookService {

   List<BookResponse> getAllBooks();
}
