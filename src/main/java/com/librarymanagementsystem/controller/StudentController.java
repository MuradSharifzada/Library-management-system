package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.StudentRequest;
import com.librarymanagementsystem.dto.response.StudentResponse;
import com.librarymanagementsystem.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new student", description = "Adds a new student to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<String> createStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the student to create",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"FIN\": \"FIN67890\", \"firstName\": \"Murad\", \"lastName\": \"Sharifzada\", " +
                                            "\"email\": \"muradsharifzada@gmail.com\", \"phoneNumber\": \"+994987654321\", " +
                                            "\"studentGroup\": \"232K eng \", \"birthDate\": \"2005-11-15\"}")))
            @RequestBody @Valid StudentRequest request) {
        studentService.createStudents(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("New Student Created successfully");
    }

    @Operation(summary = "Get all students", description = "Retrirves a list of all students with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of students retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(
            @Parameter(description = "Page number for pagination, defaults to 0", example = "0")
            @RequestParam(defaultValue = "0", name = "page Number") int page,
            @Parameter(description = "Page size for pagination, defaults to 10", example = "10")
            @RequestParam(defaultValue = "10", name = "Page Size") int size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.getAllStudents(page, size));
    }

    @Operation(summary = "Delete a student by ID", description = "Deletes a student from the system using their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(
            @Parameter(description = "ID of the student to delete") @PathVariable(name = "id") Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Student deleted successfully");
    }

    @Operation(summary = "Get a student by ID", description = "Retrieves the details of a specific student by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @Parameter(description = "ID of the student to retrieve") @PathVariable(name = "id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.getStudentById(id));
    }

    @Operation(summary = "Update a student by ID", description = "Updates the details of a specific student by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudentById(
            @Parameter(description = "ID of the student to update") @PathVariable(name = "id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated details of the student",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"FIN\": \"FIN67890\", \"firstName\": \"Murad\", \"lastName\": \"Sharifzada\", " +
                                            "\"email\": \"muradsharifzada@gmail.com\", \"phoneNumber\": \"+994987654321\", " +
                                            "\"studentGroup\": \"232K eng \", \"birthDate\": \"2005-01-01\"}")))
            @Valid @RequestBody StudentRequest request) {
        studentService.updateStudent(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Student Updated Successfully");
    }
}
