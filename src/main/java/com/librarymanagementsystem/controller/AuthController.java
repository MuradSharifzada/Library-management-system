package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.request.LoginRequest;
import com.librarymanagementsystem.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
    @PostMapping("/authenticate")
    public String authenticate(@ModelAttribute @Valid LoginRequest request, HttpSession session, Model model) {
        log.info("Received authentication request for email: {}", request.getEmail());
        try {
            authService.login(request, session);
            return "redirect:/home";
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }


}
