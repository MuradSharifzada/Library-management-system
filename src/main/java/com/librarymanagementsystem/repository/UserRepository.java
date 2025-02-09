package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.entity.User;
import com.librarymanagementsystem.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);

    boolean existsByEmailAndRole(String email, Role role);

    long countByRole(Role role);

    ;
}
