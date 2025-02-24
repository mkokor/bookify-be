package com.bookify.rest.library;

import com.bookify.api.AuthenticationService;

import com.bookify.api.LibraryService;
import com.bookify.api.model.library.LibraryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "library", description = "Library API")
@RestController
@RequestMapping(value = "/libraries", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class LibraryRestService {

  private final LibraryService libraryService;

  @GetMapping("/authorization-employee")
  public Object employee() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @GetMapping("/authorization-customer")
  public Object customer() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Operation(summary = "Get all libraries")
  @GetMapping(value = "/all")
  public ResponseEntity<List<LibraryResponse>> getAllLibraries() {
    List<LibraryResponse> libraries = libraryService.getAllLibraries();
    return new ResponseEntity<>(libraries, HttpStatus.OK);
  }
}
