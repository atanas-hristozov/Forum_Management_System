package com.example.forum_management_system.models;

import jakarta.persistence.Column;

public class UserRightsDto {
    private boolean isAdmin;
    private boolean isBanned;

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
