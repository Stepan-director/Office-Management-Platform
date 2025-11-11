package com.example.Authentication_service.dto;

import com.example.Authentication_service.model.Role;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class CreateProfileRequest {
    private Long id;
    private String employeeId;
    private String fullName;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;

    public CreateProfileRequest(Long id, String employeeId, String fullName, Role role, boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public CreateProfileRequest() {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
