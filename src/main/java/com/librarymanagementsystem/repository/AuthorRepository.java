package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByFirstNameAndLastName(String firstName, String lastName);




}
