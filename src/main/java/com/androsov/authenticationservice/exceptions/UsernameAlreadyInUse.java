package com.androsov.authenticationservice.exceptions;

public class UsernameAlreadyInUse extends RuntimeException {
    public UsernameAlreadyInUse(String message) {
        super(message);
    }
}