package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.request.LoginRequest;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    void login(LoginRequest request, HttpSession session);
}
