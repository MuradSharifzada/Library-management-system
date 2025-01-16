package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.entity.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {




    boolean existsByFIN(String FIN);

    boolean existsByEmail(String value);
}
