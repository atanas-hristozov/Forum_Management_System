package com.example.forum_management_system.exceptions;

public class AuthorizationException extends RuntimeException{

    public AuthorizationException(String message) {
        super("You are not authorized or You are banned!");
    }

}
