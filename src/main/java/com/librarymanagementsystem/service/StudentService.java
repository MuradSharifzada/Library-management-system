package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.StudentRequest;
import com.librarymanagementsystem.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    void createStudents(StudentRequest request);

    Page<StudentResponse> getAllStudents(int page, int size);

    StudentResponse getStudentById(Long id);

    void updateStudent(Long id, StudentRequest request);

    void deleteStudentById(Long id);

    Long countStudents();
}
