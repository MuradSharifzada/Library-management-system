package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.entity.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {




    boolean existsByFin(String FIN);

    boolean existsByEmail(String value);

    Optional<Student> findByFin(String fin);

    Optional<Student> findByEmail(String email);
}
