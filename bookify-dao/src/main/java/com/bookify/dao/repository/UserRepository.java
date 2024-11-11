package com.bookify.dao.repository;

import com.bookify.dao.model.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  Optional<UserEntity> findByUsername(final String username);

  Optional<UserEntity> findByEmail(final String email);

  Optional<UserEntity> findById(final UUID uuid);

  boolean existsByUsername(final String username);

  boolean existsByEmail(final String email);

}