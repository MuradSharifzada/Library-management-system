package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.StudentRequest;
import com.librarymanagementsystem.dto.response.StudentResponse;
import com.librarymanagementsystem.exception.ResourceAlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.mapper.StudentMapper;
import com.librarymanagementsystem.model.entity.Student;
import com.librarymanagementsystem.repository.StudentRepository;
import com.librarymanagementsystem.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(value = MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    StudentRequest studentRequest;
    Student student;
    StudentResponse studentResponse;

    int page;
    int size;

    @BeforeEach
    void setUp() {

        studentRequest = StudentRequest.builder()
                .fin("F2D7A9W")
                .firstName("Murad")
                .lastName("Sharifzada")
                .email("example@gmail.com")
                .studentGroup("Group 232")
                .phoneNumber("0519000000")
                .birthDate(LocalDate.of(2005, 8, 23))
                .build();

        student = Student.builder()
                .id(1L)
                .fin(studentRequest.getFin())
                .firstName(studentRequest.getFirstName())
                .lastName(studentRequest.getLastName())
                .birthDate(studentRequest.getBirthDate())
                .email(studentRequest.getEmail())
                .studentGroup(studentRequest.getStudentGroup())
                .phoneNumber(studentRequest.getPhoneNumber())
                .enrollmentDate(LocalDateTime.now())
                .build();

        studentResponse = StudentResponse.builder()
                .fin(student.getFin())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .studentGroup(student.getStudentGroup())
                .phoneNumber(student.getPhoneNumber())
                .build();

        page = 0;
        size = 10;

    }

    @Test
    void givenCreateStudentWhenFinUniqueThenSaveStudent() {

        when(studentRepository.existsByFin(studentRequest.getFin())).thenReturn(false);
        when(studentMapper.requestToEntity(studentRequest)).thenReturn(student);

        studentService.createStudents(studentRequest);

        verify(studentRepository, times(1)).save(student);
        verify(studentMapper, times(1)).requestToEntity(studentRequest);
        verify(studentRepository, times(1)).existsByFin(studentRequest.getFin());
        verifyNoMoreInteractions(studentRepository, studentMapper);

        assertEquals(studentRequest.getFin(), student.getFin());
        assertEquals(studentRequest.getEmail(), student.getEmail());

    }

    @Test
    void givenCreateStudentsWhenFinExistThenThrowResourceAlreadyExistException() {
        when(studentRepository.existsByFin(studentRequest.getFin())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> studentService.createStudents(studentRequest));

        verify(studentRepository, times(1)).existsByFin(studentRequest.getFin());
        verify(studentRepository, never()).save(any(Student.class));

    }

    @Test
    void givenGetAllStudentsWhenStudentsExistThenReturnStudentPage() {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Student> studentPage = new PageImpl<>(List.of(student));

        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        when(studentMapper.entityToResponse(student)).thenReturn(studentResponse);

        Page<StudentResponse> responses = studentService.getAllStudents(0, 10);

        verify(studentRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(studentRepository, studentMapper);

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.getTotalElements());


    }

    @Test
    void givenGetAllStudentsWhenNoStudentsExistThenReturnEmptyPage() {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        when(studentRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<StudentResponse> responses = studentService.getAllStudents(page, size);

        verify(studentRepository, times(1)).findAll(pageable);

        assertTrue(responses.isEmpty());

    }

    @Test
    void givenGetStudentByIdWhenValidIdThenReturnStudentResponse() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.entityToResponse(student)).thenReturn(studentResponse);

        StudentResponse response = studentService.getStudentById(1L);

        assertNotNull(response);
        verify(studentRepository, times(1)).findById(1L);
        verify(studentMapper, times(1)).entityToResponse(student);
        verifyNoMoreInteractions(studentRepository, studentMapper);

    }

    @Test
    void givenGetStudentByIdWhenStudentNotFoundThenThrowResourceNotFoundException() {

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));

        verify(studentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(studentRepository, studentMapper);

    }


    @Test
    void givenUpdateStudentWhenValidRequestThenUpdateStudentDetails() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.updateStudent(1L, studentRequest);

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));


        verifyNoMoreInteractions(studentRepository, studentMapper);
    }

    @Test
    void givenDeleteStudentByIdWhenValidRequestThenDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudentById(1L);

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(studentRepository);
    }
    @Test
    void givenDeleteStudentByIdWhenStudentNotFoundThenThrowResourceNotFoundException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudentById(1L));

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).deleteById(anyLong());
    }


}