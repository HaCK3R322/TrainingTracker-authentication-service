package com.androsov.authenticationservice.exceptions;

public class PasswordDoesntMatches extends RuntimeException {
    public PasswordDoesntMatches(String message) {
        super(message);
    }
}
