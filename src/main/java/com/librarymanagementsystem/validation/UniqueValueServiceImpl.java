package com.librarymanagementsystem.validation;

import com.librarymanagementsystem.repository.AuthorRepository;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.CategoryRepository;
import com.librarymanagementsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniqueValueServiceImpl implements UniqueValueService {

    private final AuthorRepository authorRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public boolean isValueUnique(String value, Class<?> entityType) {
        return switch (entityType.getSimpleName()) {
            case "StudentService" -> isStudentValueUnique(value);
            case "BookService" -> !bookRepository.existsByName(value);
            case "CategoryService" -> !categoryRepository.existsByName(value);
            default -> true;
        };
    }

    private boolean isStudentValueUnique(String value) {
        return !studentRepository.existsByFIN(value) && !studentRepository.existsByEmail(value);
    }

}
