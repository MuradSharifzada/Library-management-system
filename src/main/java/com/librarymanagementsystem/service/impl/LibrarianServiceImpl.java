package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.LibrarianRequest;
import com.librarymanagementsystem.dto.response.LibrarianResponse;
import com.librarymanagementsystem.exception.CustomIllegalStateException;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.LibrarianMapper;
import com.librarymanagementsystem.model.entity.User;
import com.librarymanagementsystem.model.enums.Role;
import com.librarymanagementsystem.repository.UserRepository;
import com.librarymanagementsystem.service.LibrarianService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibrarianServiceImpl implements LibrarianService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LibrarianMapper librarianMapper;

    @Override
    @Transactional
    public void createLibrarian(LibrarianRequest request) {
        log.info("Creating librarian with email: {}", request.getEmail());

        boolean emailExists = userRepository.existsByEmailAndRole(request.getEmail(), Role.LIBRARIAN);
        if (emailExists) {
            log.warn("Trying to create librarian, but email already in use: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already in exist!");
        }

        User librarian = librarianMapper.requestToEntity(request);
        librarian.setPassword(passwordEncoder.encode(request.getPassword()));
        librarian.setRole(Role.LIBRARIAN);
        userRepository.save(librarian);

        log.info("Librarian successfully created: {}", request.getEmail());
    }


    @Override
    public Page<LibrarianResponse> getAllLibrarians(int page, int size) {
        log.info("Fetching all librarians - Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<User> librarianPage = userRepository.findByRole(Role.LIBRARIAN, pageable);

        return librarianPage.map(librarianMapper::entityToResponse);
    }


    @Override
    public LibrarianResponse getLibrarianById(Long id) {
        log.info("Fetching librarian with ID: {}", id);

        User librarian = userRepository.findById(id)
                .filter(user -> user.getRole() == Role.LIBRARIAN)
                .orElseThrow(() -> {
                    log.warn("Librarian not found with ID: {}", id);
                    return new ResourceNotFoundException("Librarian not found");
                });

        return librarianMapper.entityToResponse(librarian);
    }

    @Override
    @Transactional
    public void updateLibrarian(Long id, LibrarianRequest request) {
        log.info("Trying to Update librarian with ID: {}", id);

        User librarian = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian not found"));

        if (librarian.getRole() != Role.LIBRARIAN) {
            log.warn("Attempted to update a non-librarian user with ID: {}", id);
            throw new CustomIllegalStateException("User is not a librarian!");
        }

        librarian.setUsername(request.getUsername());
        librarian.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            librarian.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(librarian);
        log.info("Librarian updated successfully: {}", request.getEmail());
    }

    @Override
    public void deleteLibrarian(Long id) {
        log.info("Deleting librarian with ID: {}", id);

        User librarian = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian not found"));

        if (librarian.getRole() != Role.LIBRARIAN) {
            log.warn("Trying to delete a non-librarian user with ID: {}", id);
            throw new CustomIllegalStateException("User is not a librarian!");
        }

        userRepository.delete(librarian);
        log.info("Librarian deleted successfully: {}", librarian.getEmail());
    }

    @Override
    public Long countLibrarians() {
        return userRepository.countByRole(Role.LIBRARIAN);
    }
}
