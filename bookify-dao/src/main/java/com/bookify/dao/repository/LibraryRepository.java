package com.bookify.dao.repository;

import com.bookify.dao.model.LibraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LibraryRepository extends JpaRepository<LibraryEntity, UUID> {


    List<LibraryEntity> findAll();

}