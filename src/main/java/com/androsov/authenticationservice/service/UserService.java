package com.androsov.authenticationservice.service;

import com.androsov.authenticationservice.entity.User;
import com.androsov.authenticationservice.exceptions.UsernameAlreadyInUse;
import com.androsov.authenticationservice.exceptions.UsernameNotFoundException;
import com.androsov.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    public User saveToDatabase(User user) throws UsernameAlreadyInUse {
        if(userRepository.existsByUsername(user.getUsername()))
            throw new UsernameAlreadyInUse("Username " + user.getUsername() + " already exists");

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User loadFromDatabase(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isEmpty())
            throw new UsernameNotFoundException("User with username " + username + " not found!");

        return userOptional.get();
    }

    public boolean isPasswordCorrect(String username, String password) throws UsernameNotFoundException {
        User databaseUser = loadFromDatabase(username);
        return encoder.matches(password, databaseUser.getPassword());
    }
}
