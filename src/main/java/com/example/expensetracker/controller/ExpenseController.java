package com.example.expensetracker.controller;

import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.IExpenseRepository;
import com.example.expensetracker.repository.IUserRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expense")
@PermitAll
public class ExpenseController {
    @Autowired
    private IExpenseRepository expenseRepository;
    @Autowired
    private IUserRepository userRepository;
    @PostMapping("/create")
    public @ResponseBody Expense createExpense(@RequestBody Expense expense, @RequestParam Integer userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalStateException("User doesn't exist!");
        }
        expense.setUser(user.get());
        return expenseRepository.save(expense);
    }

    @GetMapping("/user")
    public @ResponseBody List<Expense> getAllExpensesByUserId(@RequestParam Integer userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalStateException("User doesn't exist!");
        }
        return  expenseRepository.getExpenseByUserId(userId);
    }
}
