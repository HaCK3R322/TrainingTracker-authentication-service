package com.androsov.authenticationservice.controller;

import com.androsov.authenticationservice.dto.*;
import com.androsov.authenticationservice.entity.User;
import com.androsov.authenticationservice.exceptions.PasswordDoesntMatches;
import com.androsov.authenticationservice.exceptions.UsernameAlreadyInUse;
import com.androsov.authenticationservice.exceptions.UsernameNotFoundException;
import com.androsov.authenticationservice.service.JwtService;
import com.androsov.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    @ExceptionHandler({
            UsernameAlreadyInUse.class,
            UsernameNotFoundException.class,
            PasswordDoesntMatches.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ExceptionResponse handleUserExceptions(Exception ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    // register
    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody RegistrationRequest request) {
        User user = new User(
                null,
                request.getUsername(),
                request.getPassword(),
                "ROLE_USER"
        );

        User savedUser = userService.saveToDatabase(user);

        return jwtService.generateToken(savedUser.getUsername(), savedUser.getAuthorities());
    }

    @PostMapping(path = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public String authenticate(@RequestBody AuthenticationRequest request) {
        if(userService.isPasswordCorrect(request.getUsername(), request.getPassword())) {

            User user = userService.loadFromDatabase(request.getUsername());

            return jwtService.generateToken(request.getUsername(), user.getAuthorities());

        } else {
            throw new PasswordDoesntMatches("Password doesn't matches for username " + request.getUsername());
        }
    }

    @PostMapping(path = "/validate")
    @ResponseStatus(HttpStatus.OK)
    public String validate(@RequestBody JwtToken request) {
        if(jwtService.isValid(request.getToken())) {
            return "ok";
        } else {
            return "not valid";
        }
    }

    @PostMapping(path = "/parse")
    public ResponseEntity<UsernameAuthoritiesResponse> parse(@RequestBody JwtToken request) {
        UsernameAuthoritiesResponse response = new UsernameAuthoritiesResponse(
                jwtService.getUsername(request.getToken()),
                jwtService.getAuthorities(request.getToken())
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
