package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByName(String name);

    @Query(
            value = "SELECT * FROM books WHERE name ~* :name",
            countQuery = "SELECT COUNT(*) FROM books WHERE name ~* :name",
            nativeQuery = true
    )
    Page<Book> findByName(@Param("name") String name, Pageable pageable);

    @Query(
            value = "SELECT b.* FROM books b " +
                    "JOIN categories c ON b.category_id = c.id " +
                    "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))",
            countQuery = "SELECT COUNT(b.id) FROM books b " +
                    "JOIN categories c ON b.category_id = c.id " +
                    "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))",
            nativeQuery = true
    )
    Page<Book> findBooksByCategory(@Param("name") String name, Pageable pageable);



}
