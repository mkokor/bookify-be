package com.bookify.core.mapper;

import com.bookify.api.model.role.RoleResponse;
import com.bookify.dao.model.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  RoleResponse toRoleResponse(final RoleEntity roleEntity);

}