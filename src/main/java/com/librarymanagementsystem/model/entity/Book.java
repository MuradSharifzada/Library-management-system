package com.librarymanagementsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "authors_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    List<Author> authors=new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;

    @OneToMany(mappedBy = "book")
    List<Order> orders;


}
