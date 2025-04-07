package com.librarymanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false,unique = true)
    String name;


    @Column(nullable = false)
    String description;

    @Column(nullable = false,unique = true)
    String type;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy = "category")
    List<Book> books;
}
