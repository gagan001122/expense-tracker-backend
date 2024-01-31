package com.example.expensetracker.controller;

import com.example.expensetracker.dto.LoginRequest;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.IUserRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@PermitAll
public class UserController {
    @Autowired
    private IUserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public @ResponseBody User createUser(@RequestBody User user){
        Optional<User> existingUser = repository.getUserByEmail(user.getEmail());
        if(existingUser.isPresent()){
            throw new IllegalStateException("Account with email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @PostMapping("/login")
    public @ResponseBody String authenticateUser(@RequestBody LoginRequest req){
        Optional<User> existingUser = repository.getUserByEmail(req.getEmail());
        if(existingUser.isEmpty()){
            throw new IllegalStateException("Account doesn't exist!");
        }
        if(!passwordEncoder.matches(req.getPassword(),existingUser.get().getPassword())){
            throw new IllegalStateException("Wrong Password!");
        }
        return "Success";
    }
}
