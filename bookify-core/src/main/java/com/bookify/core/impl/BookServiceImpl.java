package com.bookify.core.impl;

import com.bookify.api.BookService;
import com.bookify.api.model.book.BookRequest;
import com.bookify.api.model.book.BookResponse;
import com.bookify.api.model.exception.ApiException;
import com.bookify.core.mapper.BookMapper;
import com.bookify.dao.model.BookEntity;
import com.bookify.dao.repository.BookRepository;

import com.bookify.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
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
}




