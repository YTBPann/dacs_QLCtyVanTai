package com.ytbpann.quanlyvantai.user.dto;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequest {

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    private boolean enabled = true;

    public UserUpdateRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}