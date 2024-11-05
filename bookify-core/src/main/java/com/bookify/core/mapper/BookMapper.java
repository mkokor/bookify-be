package com.bookify.core.mapper;

import com.bookify.api.model.book.BookResponse;
import com.bookify.dao.model.BookEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface BookMapper{

    BookResponse toBookResponse(final BookEntity bookEntity);

}
