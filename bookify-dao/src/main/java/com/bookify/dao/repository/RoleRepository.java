package com.bookify.dao.repository;

import com.bookify.dao.model.RoleEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

  Optional<RoleEntity> findByName(final String name);

}