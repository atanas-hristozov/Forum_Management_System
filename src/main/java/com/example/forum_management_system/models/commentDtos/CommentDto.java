package com.example.forum_management_system.models.commentDtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CommentDto {
    @Column(name = "text")
    @NotEmpty(message = "text can't be empty!")
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
