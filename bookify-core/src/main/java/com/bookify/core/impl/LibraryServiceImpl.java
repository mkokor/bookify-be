package com.bookify.core.impl;

import com.bookify.api.LibraryService;
import com.bookify.api.TokenService;
import com.bookify.api.enums.ApiErrorType;
import com.bookify.api.model.error.ApiError;
import com.bookify.api.model.exception.ApiException;
import com.bookify.api.model.library.LibraryResponse;
import com.bookify.dao.model.LibraryEntity;
import com.bookify.dao.model.UserEntity;
import com.bookify.dao.repository.LibraryRepository;
import com.bookify.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;

    @Override
    public List<LibraryResponse> getAllLibraries() {
        List<LibraryEntity> libraries = libraryRepository.findAll();
        List<LibraryResponse> libraryResponses = new ArrayList<>();

        for (LibraryEntity libraryEntity : libraries) {
            LibraryResponse response = new LibraryResponse();
            response.setId(libraryEntity.getId());
            response.setName(libraryEntity.getName());
            response.setAddress(libraryEntity.getAddress());
            response.setPhone(libraryEntity.getPhone());
            response.setEmail(libraryEntity.getEmail());
            response.setImage(libraryEntity.getImage());

            libraryResponses.add(response);
        }

        return libraryResponses;
    }
    
}
