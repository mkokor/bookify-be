package com.bookify.core.impl;

import com.bookify.api.BookService;
import com.bookify.api.TokenService;
import com.bookify.api.enums.ApiErrorType;
import com.bookify.api.model.book.BookRequest;
import com.bookify.api.model.book.BookResponse;
import com.bookify.api.model.error.ApiError;
import com.bookify.api.model.exception.ApiException;
import com.bookify.core.mapper.BookMapper;
import com.bookify.dao.model.BookEntity;
import com.bookify.dao.model.UserEntity;
import com.bookify.dao.repository.BookRepository;
import com.bookify.dao.repository.BookReservationRepository;
import com.bookify.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;

    private final BookReservationRepository bookReservationRepository;
    private final TokenService tokenService;

    @Override
    public List<BookResponse> getAllBooks() {
        List<BookEntity> books = bookRepository.findAll();
        List<BookResponse> bookResponses = new ArrayList<>();

        for (BookEntity bookEntity : books) {
            BookResponse response = new BookResponse();
            response.setId(bookEntity.getId());
            response.setIssueDate(bookEntity.getIssueDate());
            response.setAuthor(bookEntity.getAuthor());
            response.setGenre(bookEntity.getGenre());
            response.setTitle(bookEntity.getTitle());
            response.setDescription(bookEntity.getDescription());
            response.setCopiesAvailable(bookEntity.getCopiesAvailable());
            response.setNumberOfPages(bookEntity.getNumberOfPages());
            response.setCoverImage(bookEntity.getBookUrl());

            bookResponses.add(response);
        }

        return bookResponses;
    }



    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(bookRequest.getTitle());
        bookEntity.setAuthor(bookRequest.getAuthor());
        bookEntity.setIssueDate(bookRequest.getIssueDate());
        bookEntity.setGenre(bookRequest.getGenre());
        bookEntity.setDescription(bookRequest.getDescription());
        bookEntity.setNumberOfPages(bookRequest.getNumberOfPages());
        bookEntity.setCopiesAvailable(bookRequest.getCopiesAvailable());
        bookEntity.setBookUrl(bookRequest.getBookUrl());

        BookEntity savedBook = bookRepository.save(bookEntity);
        return bookMapper.toBookResponse(savedBook);
    }

    @Override
    public BookResponse getBookById(UUID id) {
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> {

                    ApiError apiError = new ApiError(ApiErrorType.BUSINESS_LOGIC, "Book not found with id: " + id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, apiError.getMessage());
                });

        BookResponse response = new BookResponse();
        response.setId(bookEntity.getId());
        response.setIssueDate(bookEntity.getIssueDate());
        response.setAuthor(bookEntity.getAuthor());
        response.setGenre(bookEntity.getGenre());
        response.setTitle(bookEntity.getTitle());
        response.setDescription(bookEntity.getDescription());
        response.setCopiesAvailable(bookEntity.getCopiesAvailable());
        response.setNumberOfPages(bookEntity.getNumberOfPages());
        response.setCoverImage(bookEntity.getBookUrl());

        return response;
    }

    @Override
    public BookResponse getBookByTitle(String title) {
        BookEntity bookEntity = bookRepository.findByTitle(title)
                .orElseThrow(() -> {

                    ApiError apiError = new ApiError(ApiErrorType.BUSINESS_LOGIC, "Book not found with title: " + title);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, apiError.getMessage());
                });

        BookResponse response = new BookResponse();
        response.setId(bookEntity.getId());
        response.setIssueDate(bookEntity.getIssueDate());
        response.setAuthor(bookEntity.getAuthor());
        response.setGenre(bookEntity.getGenre());
        response.setTitle(bookEntity.getTitle());
        response.setDescription(bookEntity.getDescription());
        response.setCopiesAvailable(bookEntity.getCopiesAvailable());
        response.setNumberOfPages(bookEntity.getNumberOfPages());
        response.setCoverImage(bookEntity.getBookUrl());

        return response;
    }

    @Override
    public void deleteBookById(UUID id) {
        bookRepository.findById(id).orElseThrow(() -> {
            ApiError apiError = new ApiError(ApiErrorType.BUSINESS_LOGIC, "Book not found with id: " + id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, apiError.getMessage());
        });

        bookRepository.deleteById(id);
    }

    @Override
    public boolean isBookReserved(UUID bookId) {
        return bookRepository.findById(bookId)
                .map(book -> !book.getUsers().isEmpty())
                .orElse(false);
    }

    @Transactional
    @Override
    public void reserveBook(String authHeader, UUID bookId) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
            }
            String token = authHeader.substring(7);
            String username = tokenService.getAccessTokenOwner(token);// Remove "Bearer " prefix
            UserEntity user = userRepository.findByUsername(username)

                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            System.out.println(user);
            BookEntity book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

            long reservedCopiesCount = bookReservationRepository.countByBookId(bookId);
            if (reservedCopiesCount >= book.getCopiesAvailable()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All copies of this book are already reserved");
            }

            if (user.getBooks().contains(book)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book is already reserved by this user");
            }
            user.getBooks().add(book);
            userRepository.save(user);
        } catch (ResponseStatusException e) {
            throw e;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BookResponse> getReservationsAdmin(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Set<BookEntity> reservedBooks = user.getBooks();

        List<BookResponse> bookResponses = new ArrayList<>();
        for (BookEntity bookEntity : reservedBooks) {
            BookResponse response = new BookResponse();
            response.setId(bookEntity.getId());
            response.setIssueDate(bookEntity.getIssueDate());
            response.setAuthor(bookEntity.getAuthor());
            response.setGenre(bookEntity.getGenre());
            response.setTitle(bookEntity.getTitle());
            response.setDescription(bookEntity.getDescription());
            response.setCopiesAvailable(bookEntity.getCopiesAvailable());
            response.setNumberOfPages(bookEntity.getNumberOfPages());
            response.setCoverImage(bookEntity.getBookUrl());

            bookResponses.add(response);
        }

        return bookResponses;
    }

    public List<BookResponse> getReservationsUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
        }

        String token = authHeader.substring(7);
        String username = tokenService.getAccessTokenOwner(token);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Set<BookEntity> reservedBooks = user.getBooks();

        List<BookResponse> bookResponses = new ArrayList<>();
        for (BookEntity bookEntity : reservedBooks) {
            BookResponse response = new BookResponse();
            response.setId(bookEntity.getId());
            response.setIssueDate(bookEntity.getIssueDate());
            response.setAuthor(bookEntity.getAuthor());
            response.setGenre(bookEntity.getGenre());
            response.setTitle(bookEntity.getTitle());
            response.setDescription(bookEntity.getDescription());
            response.setCopiesAvailable(bookEntity.getCopiesAvailable());
            response.setNumberOfPages(bookEntity.getNumberOfPages());
            response.setCoverImage(bookEntity.getBookUrl());

            bookResponses.add(response);
        }

        return bookResponses;
    }

    @Transactional
    @Override
    public void deleteReservationUser(String authHeader, UUID bookId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing or invalid");
        }

        String token = authHeader.substring(7);
        String username = tokenService.getAccessTokenOwner(token);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        if (!user.getBooks().contains(book)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation not found for this user and book");
        }

        user.getBooks().remove(book);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteReservationAdmin(UUID userId, UUID bookId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        if (!user.getBooks().contains(book)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation not found for this user and book");
        }

        user.getBooks().remove(book);
        userRepository.save(user);
    }

}




