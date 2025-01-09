package com.librarymanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "books")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String isbn;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    String description;


    String bookImage;

    @Column(nullable = false)
    int stockCount;

    @Column(name = "published_time")
    LocalDate publishedTime;

    @ManyToMany(mappedBy = "books")
    List<Author> authors;

    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;

    @OneToMany(mappedBy = "book")
    List<Order> orders;


}