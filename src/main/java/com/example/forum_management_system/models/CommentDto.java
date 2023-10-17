package com.example.forum_management_system.Models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public class CommentDto {
    @Column(name = "text")
    @NotNull(message = "text can't be empty!")
    private String text;
    public CommentDto(){

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
