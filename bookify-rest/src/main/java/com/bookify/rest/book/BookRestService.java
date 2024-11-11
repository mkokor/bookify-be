package com.bookify.rest.book;

import com.bookify.api.AuthenticationService;
import com.bookify.api.BookService;
import com.bookify.api.model.book.BookRequest;
import com.bookify.api.model.book.BookReservationRequest;
import com.bookify.api.model.book.BookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "book", description = "Book API")
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookRestService {

  private final BookService bookService;

  @GetMapping("/authorization-employee")
  public Object employee() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @GetMapping("/authorization-customer")
  public Object customer() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Operation(summary = "Get all books")
  @GetMapping(value = "/all")
  public ResponseEntity<List<BookResponse>> getAllUsers() {
    List<BookResponse> books = bookService.getAllBooks();
    return new ResponseEntity<>(books, HttpStatus.OK);
  }


  @Operation(summary = "Create a new book")
  @PostMapping(value = "/create")
  public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest) {
    BookResponse bookResponse = bookService.createBook(bookRequest);
    return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
  }

  @Operation(summary = "Get book by ID")
  @GetMapping(value = "/{id}")
  public ResponseEntity<BookResponse> getBookById(@PathVariable UUID id) {
    BookResponse bookResponse = bookService.getBookById(id);
    return new ResponseEntity<>(bookResponse, HttpStatus.OK);
  }

  @Operation(summary = "Get book by title")
  @GetMapping(value = "/findByTitle")
  public ResponseEntity<BookResponse> getByTitle(@RequestParam String title) {
    BookResponse bookResponse = bookService.getBookByTitle(title);
    return new ResponseEntity<>(bookResponse, HttpStatus.OK);
  }

  @Operation(summary = "Delete book by ID")
  @DeleteMapping(value = "/delete/{id}")
  public ResponseEntity<Void> deleteBookById(@PathVariable UUID id) {
    bookService.deleteBookById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary = "Reserve a book for a user")
  @PostMapping(value = "/reserve")
  public ResponseEntity<String> reserveBook(@RequestBody BookReservationRequest request) {
    bookService.reserveBook(request.getUserId(), request.getBookId());
    return new ResponseEntity<>("Book reserved successfully", HttpStatus.OK);
  }


  @Operation(summary = "Check if a book is reserved")
  @GetMapping(value = "/reservation/{bookId}")
  public ResponseEntity<Boolean> isBookReserved(@PathVariable UUID bookId){
    boolean isReserved = bookService.isBookReserved(bookId);
    return ResponseEntity.ok(isReserved);
  }


}