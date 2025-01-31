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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public void createStudents(StudentRequest request) {
        log.info("Trying to create a new student with name: {}", request.getFirstName());

        if (studentRepository.existsByFin(request.getFin())) {
            log.error("Student creation failed - FIN {} already exists", request.getFin());
            throw new ResourceAlreadyExistsException("FIN " + request.getFin() + " Already exist");
        }
        studentRepository.save(studentMapper.requestToEntity(request));

        log.info("Successfully created new student with name: {}", request.getFirstName());

    }

    @Override
    public Page<StudentResponse> getAllStudents(int page, int size) {
        log.info("Trying to retrieve all students with page {}", page);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        log.info("Retrieved students (page: {}, size: {})", page, size);
        return studentRepository
                .findAll(pageable)
                .map(studentMapper::entityToResponse);
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

        if (!existingStudent.getFin().equals(request.getFin())) {
            Optional<Student> studentWithSameFin = studentRepository.findByFin(request.getFin());
            if (studentWithSameFin.isPresent()) {
                throw new ResourceAlreadyExistsException("Student FIN must be unique.");
            }
            existingStudent.setFin(request.getFin());
        }

        if (!existingStudent.getEmail().equals(request.getEmail())) {
            Optional<Student> studentWithSameEmail = studentRepository.findByEmail(request.getEmail());
            if (studentWithSameEmail.isPresent()) {
                throw new ResourceAlreadyExistsException("Student Email must be unique.");
            }
            existingStudent.setEmail(request.getEmail());
        }


        existingStudent.setFirstName(request.getFirstName());
        existingStudent.setLastName(request.getLastName());
        existingStudent.setPhoneNumber(request.getPhoneNumber());
        existingStudent.setStudentGroup(request.getStudentGroup());
        existingStudent.setBirthDate(request.getBirthDate());

        studentRepository.save(existingStudent);
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

    @Override
    public Long countStudents() {
        return studentRepository.count();
    }
}
