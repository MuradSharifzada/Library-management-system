package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.entity.Book;
import com.librarymanagementsystem.model.entity.Order;
import com.librarymanagementsystem.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {



    Optional<Order> findByStudentIdAndBookIdAndReturnDateIsNull(Long studentId, Long bookId);

    boolean existsByStudentAndReturnDateIsNull(Student student);
}
