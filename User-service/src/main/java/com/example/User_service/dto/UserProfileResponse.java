package com.example.User_service.dto;

import com.example.User_service.model.Role;
import com.example.User_service.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class UserProfileResponse {
    private Long id;
    private String employeeId;
    private String fullName;
    private Role role;
    private boolean active;
    private String email;
    private String phone;
    private LocalDateTime createdAt;

    public UserProfileResponse(User user) {
        this.id = id;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public UserProfileResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
