package com.librarymanagementsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String fin;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(length = 15, nullable = false)
    String studentGroup;


    LocalDate birthDate;


    @Builder.Default
    LocalDateTime enrollmentDate = LocalDateTime.now();

    @OneToMany(mappedBy = "student")
    List<Order> orders;
}
