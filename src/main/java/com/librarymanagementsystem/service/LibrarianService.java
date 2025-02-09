package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.LibrarianRequest;
import com.librarymanagementsystem.dto.response.LibrarianResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface LibrarianService {

    void createLibrarian(LibrarianRequest request);

    Page<LibrarianResponse> getAllLibrarians(int page,int size);

    LibrarianResponse getLibrarianById(Long id);

    void updateLibrarian(Long id, LibrarianRequest request);

    void deleteLibrarian(Long id);

    Long countLibrarians();
}
