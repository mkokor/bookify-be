package com.bookify.dao.repository;

import com.bookify.dao.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BookReservationRepository extends JpaRepository<UserEntity, UUID> {

    @Query(value = "SELECT COUNT(*) FROM book_reservation WHERE book_id = :bookId", nativeQuery = true)
    long countByBookId(@Param("bookId") UUID bookId);
}
