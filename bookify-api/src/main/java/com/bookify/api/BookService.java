package com.bookify.api;
import com.bookify.api.model.book.BookRequest;
import com.bookify.api.model.book.BookResponse;

import java.util.List;
import java.util.UUID;


public interface BookService {

   List<BookResponse> getAllBooks();
   BookResponse createBook(BookRequest bookRequest);
   BookResponse getBookById(UUID id);
   BookResponse getBookByTitle(String title);
   void deleteBookById(UUID id);
   boolean isBookReserved(UUID bookId);
   void reserveBook(String authHeader, UUID book_id);
   List<BookResponse> getReservationsAdmin(UUID userId);
   List<BookResponse> getReservationsUser(String authHeader);
   void deleteReservationUser(String authHeader, UUID bookId);
   void deleteReservationAdmin(UUID userId, UUID bookId);
}
