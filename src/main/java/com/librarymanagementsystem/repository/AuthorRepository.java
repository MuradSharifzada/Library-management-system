package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.entity.Author;
import com.librarymanagementsystem.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByFirstNameAndLastName(String firstName, String lastName);



}
