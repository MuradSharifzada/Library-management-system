package com.librarymanagementsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(nullable = false)
    @CreationTimestamp
    LocalDateTime orderDate;


    @Column(nullable = false)
    LocalDateTime returnDate;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    Student student;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    Book book;



}
