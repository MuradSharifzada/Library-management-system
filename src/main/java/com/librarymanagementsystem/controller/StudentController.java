package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.StudentRequest;
import com.librarymanagementsystem.dto.response.StudentResponse;
import com.librarymanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody @Valid StudentRequest request) {

        studentService.createStudents(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("New Student Created successfully");
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(
            @RequestParam(defaultValue = "0",name = "page Number") int page,
            @RequestParam(defaultValue = "10",name = "Page Size") int size){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.getAllStudents(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable(name = "id") Long id) {

        studentService.deleteStudentById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Student deleted successfully");


    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable(name = "id") Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudentById(@PathVariable(name = "id") Long id, @Valid @RequestBody StudentRequest request) {

        studentService.updateStudent(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Student Updated Successfully");

    }
}
