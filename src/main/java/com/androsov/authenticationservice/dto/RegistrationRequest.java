package com.androsov.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class RegistrationRequest {
    public String username;
    public String password;
}
