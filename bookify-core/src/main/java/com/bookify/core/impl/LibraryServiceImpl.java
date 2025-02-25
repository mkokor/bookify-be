package com.bookify.core.impl;

import com.bookify.api.LibraryService;
import com.bookify.api.model.library.LibraryResponse;
import com.bookify.dao.model.LibraryEntity;
import com.bookify.dao.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
