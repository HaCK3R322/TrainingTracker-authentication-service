package com.androsov.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsernameAuthoritiesResponse {
    public String username;
    public String authorities;
}
