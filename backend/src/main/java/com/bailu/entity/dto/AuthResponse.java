package com.bailu.entity.dto;

import com.bailu.entity.Role;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private String refreshToken; // 添加刷新令牌字段
    private String username;
    private Role role;

    // 构造方法
    public AuthResponse(boolean success, String message, String token, String username, Role role) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
    }

    // 新的构造方法包含刷新令牌
    public AuthResponse(boolean success, String message, String token, String refreshToken, String username, Role role) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.role = role;
    }

    // Getter 和 Setter
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}