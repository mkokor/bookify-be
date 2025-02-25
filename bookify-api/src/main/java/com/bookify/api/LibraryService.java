package com.bookify.api;
import com.bookify.api.model.library.LibraryResponse;

import java.util.List;


public interface LibraryService {

   List<LibraryResponse> getAllLibraries();
}