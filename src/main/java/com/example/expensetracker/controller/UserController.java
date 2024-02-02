package com.example.expensetracker.controller;

import com.example.expensetracker.dto.LoginRequest;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.IUserRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
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

    @PatchMapping("/budget/{id}")
    public @ResponseBody User setBudget(@PathVariable Integer id, @RequestParam Integer budget){
        Optional<User> existingUser = repository.findById(id);
        if(existingUser.isEmpty()){
            throw new IllegalStateException("Account doesn't exist!");
        }
        User user = existingUser.get();

        user.setBudget(BigDecimal.valueOf(budget));
        return repository.save(user);
    }
    @PostMapping("/login")
    public @ResponseBody int authenticateUser(@RequestBody LoginRequest req){
        Optional<User> existingUser = repository.getUserByEmail(req.getEmail());
        if(existingUser.isEmpty()){
            throw new IllegalStateException("Account doesn't exist!");
        }
        if(!passwordEncoder.matches(req.getPassword(),existingUser.get().getPassword())){
            throw new IllegalStateException("Wrong Password!");
        }
        return existingUser.get().getId() ;
    }

    @GetMapping("/all")
    public @ResponseBody List<User> getAllUsers(){
        return (List<User>) repository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody User getUserDetails(@PathVariable Integer id){
        Optional<User> existingUser = repository.findById(id);
        if(existingUser.isEmpty()){
            throw new IllegalStateException("Account doesn't exist!");
        }
        return existingUser.get();
    }
}
