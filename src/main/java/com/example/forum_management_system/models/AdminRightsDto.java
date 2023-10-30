package com.example.forum_management_system.models;

import com.example.forum_management_system.exceptions.AuthorizationException;

import java.util.Optional;

public class AdminRightsDto {
    private boolean isAdmin;
    private boolean isBanned;
    private String phoneNumber;


    public AdminRightsDto() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}

