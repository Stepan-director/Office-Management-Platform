package com.example.User_service.dto;

import com.example.User_service.model.Role;

public class UpdateProfileRequest {
/*    private Long id;
    private String employeeId;*/
    private String fullName;
    private Role role;
    private String email;
    private String phone;
    private String workplaceId;

    public UpdateProfileRequest(String fullName, Role role, String email, String phone, String workplaceId) {
        this.fullName = fullName;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.workplaceId = workplaceId;
    }

    public UpdateProfileRequest() {
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

    public String getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(String workplaceId) {
        this.workplaceId = workplaceId;
    }
}
