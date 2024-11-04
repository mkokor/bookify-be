package com.bookify.dao.repository;

import com.bookify.dao.model.BookEntity;
import com.bookify.dao.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<BookEntity, UUID> {

    Optional<BookEntity> findByTitle(final String title);

    Optional<BookEntity> findByAuthor(final String author);

    List<BookEntity> findAll();

}