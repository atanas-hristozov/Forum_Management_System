package com.example.forum_management_system.exceptions;

public class AlreadyLikedException extends RuntimeException{
    public AlreadyLikedException() {
        super(String.format("This user already liked this post"));
    }
}
