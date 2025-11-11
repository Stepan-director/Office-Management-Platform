package com.example.Workplace_service.dto;

public class TokenInfo {
    private final String userId;
    private final String role;

    public TokenInfo(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }


    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
