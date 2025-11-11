package com.example.User_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
public class User { // надо будет сделать измение имение некоторых полей в двух бд auth и user
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId; // это передадётся из auth service

    @Column(name = "full_name", nullable = false)
    private String fullName; // это передадётся из auth service

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role; // это передадётся из auth service

    @Column(name = "active")
    private boolean active;

    @Column(nullable = true)
    private String email;
    @Column(nullable = true)
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt; // это передадётся из auth service

    public User(Long id, String employeeId, String fullName, Role role, boolean active, String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public User() {
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
