package com.example.forum_management_system.exceptions;

public class AlreadyDislikedException extends RuntimeException{
    public AlreadyDislikedException() {
        super(String.format("This user already disliked this post"));
    }
}
