package com.example.Authentication_service.controller;

import com.example.Authentication_service.dto.LoginRequest;
import com.example.Authentication_service.dto.LoginResponse;
import com.example.Authentication_service.dto.RegisterRequest;
import com.example.Authentication_service.model.AuthUser;
import com.example.Authentication_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    private ResponseEntity<?>  register(@RequestBody RegisterRequest request){
        try {
            AuthUser user = authService.registerUser(request);
            return ResponseEntity.ok("Пользователь зарегестрирован с иднексом: " + user.getId());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            LoginResponse loginResponse = authService.loginUser(request);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

}
