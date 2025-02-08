package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.StudentRequest;
import com.librarymanagementsystem.dto.response.StudentResponse;
import com.librarymanagementsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    @GetMapping
    public String showAllStudents(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        Page<StudentResponse> studentPage = studentService.getAllStudents(page, size);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "student/students";
    }


    @GetMapping("/add")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new StudentRequest());
        return "student/add-student";
    }


    @PostMapping("/add")
    public String createStudent(@Validated(StudentRequest.Create.class) @ModelAttribute StudentRequest studentRequest) {
        studentService.createStudents(studentRequest);
        return "redirect:/students";
    }


    @GetMapping("/details/{id}")
    public String getStudentById(@PathVariable Long id, Model model) {
        StudentResponse studentResponse = studentService.getStudentById(id);
        model.addAttribute("student", studentResponse);
        return "student/student-details";
    }


    @GetMapping("/update/{id}")
    public String updateStudentForm(@PathVariable Long id, Model model) {
        StudentResponse studentResponse = studentService.getStudentById(id);
        model.addAttribute("student", studentResponse);
        return "student/update-student";
    }


    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,  @Validated(StudentRequest.Update.class) @ModelAttribute StudentRequest studentRequest) {
        studentService.updateStudent(id, studentRequest);
        return "redirect:/students";
    }


    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
}
