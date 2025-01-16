package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.dto.request.StudentRequest;
import com.librarymanagementsystem.dto.response.StudentResponse;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.StudentMapper;
import com.librarymanagementsystem.model.entity.Student;
import com.librarymanagementsystem.repository.StudentRepository;
import com.librarymanagementsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public void createStudents(StudentRequest request) {
        log.info("Trying to create a new student with name: {}", request.getFirstName());

        if (studentRepository.existsByFIN(request.getFIN())) {
            log.error("Student creation failed - FIN {} already exists", request.getFIN());
            throw new ResourceAlreadyExistsException("FIN " + request.getFIN() + " Already exist");
        }
        studentRepository.save(studentMapper.requestToEntity(request));

        log.info("Successfully created new student with name: {}", request.getFirstName());

    }

    @Override
    public List<StudentResponse> getAllStudents(int page, int size) {
        log.info("Trying to retrieve all students with page {}", page);

        Pageable pageable = PageRequest.of(page, size);

        log.info("Retrieved students (page: {}, size: {})", page, size);
        return studentRepository
                .findAll(pageable).stream()
                .map(studentMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        log.info("Trying to find student by id: {}", id);

        Student student = studentRepository.findById(id).orElseThrow(() -> {
            log.error("Student not found with id: {}", id);
            return new ResourceNotFoundException("Student not found with id: " + id);
        });

        log.info("Successfully retried student with id: {}", id);
        return studentMapper.entityToResponse(student);
    }

    @Override
    public void updateStudent(Long id, StudentRequest request) {

        log.info("Trying to update student by id: {}", id);

        Student existingStudent = studentRepository.findById(id).orElseThrow(() -> {
            log.error("Updating failed: Student not found with id: {}", id);
            return new ResourceNotFoundException("Updating failed: Student not found with id: " + id);
        });

        Student updatedStudent = studentMapper.requestToEntity(request);
        updatedStudent.setId(existingStudent.getId());

        studentRepository.save(updatedStudent);
        log.info("Successfully updated Student with id: {}", id);
    }

    @Override
    public void deleteStudentById(Long id) {

        log.info("Trying to delete student by id: {}", id);

        Student student = studentRepository.findById(id).orElseThrow(() -> {
            log.error("Deleting failed: Student not found with id: {}", id);
            return new ResourceNotFoundException("Deleting failed: Student not found with id: " + id);
        });

        studentRepository.deleteById(student.getId());
        log.info("Student deleted successfully");

    }
}
