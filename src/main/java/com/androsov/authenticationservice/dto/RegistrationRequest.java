package com.androsov.authenticationservice.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    public String username;
    public String password;
}
