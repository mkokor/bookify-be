package com.bookify.core.mapper;

import com.bookify.api.model.user.UserRegistrationRequest;
import com.bookify.api.model.user.UserResponse;
import com.bookify.dao.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

  @Mapping(target = "password", ignore = true)
  UserEntity toEntity(final UserRegistrationRequest userRegistrationRequest);

  UserResponse toUserResponse(final UserEntity userEntity);

}