package com.bookify.rest.book;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "book", description = "Book API")
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookRestService {

  @GetMapping("/authorization-employee")
  public Object employee() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @GetMapping("/authorization-customer")
  public Object customer() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}