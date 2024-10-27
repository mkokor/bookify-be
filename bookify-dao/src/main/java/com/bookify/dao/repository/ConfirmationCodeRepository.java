package com.bookify.dao.repository;

import com.bookify.dao.model.ConfirmationCodeEntity;
import com.bookify.dao.model.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCodeEntity, UUID> {

  boolean existsByUserAndType(final UserEntity user, final String type);

  void deleteByUserAndType(final UserEntity user, final String type);

  Optional<ConfirmationCodeEntity> findByUserAndType(final UserEntity user, final String type);

}