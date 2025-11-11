package com.example.User_service.controller;

import com.example.User_service.dto.CreateProfileRequest;
import com.example.User_service.dto.UserProfileResponse;
import com.example.User_service.model.User;
import com.example.User_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/profiles")
    private ResponseEntity<?> createUserProfile(@RequestBody CreateProfileRequest request){
        try {
            User user = userService.createUserProfile(request);
            return ResponseEntity.ok("Профиль создан: " + user.getFullName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    @GetMapping("/{employeeId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable String employeeId) {
        try {
            UserProfileResponse profile = userService.getUserProfileByEmployeeId(employeeId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Ошибка: " + e.getMessage());
        }
    }



}
