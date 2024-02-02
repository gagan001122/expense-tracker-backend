package com.example.expensetracker.controller;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.ICategoryRepository;
import com.example.expensetracker.repository.IExpenseRepository;
import com.example.expensetracker.repository.IUserRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/expense")
@PermitAll
public class ExpenseController {
    @Autowired
    private IExpenseRepository expenseRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @PostMapping("/create")
    public @ResponseBody Expense createExpense(@RequestBody Expense expense, @RequestParam Integer userId, @RequestParam Integer categoryId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(user.isEmpty() || category.isEmpty()){
            throw new IllegalStateException("Invalid Request Params!");
        }
        expense.setUser(user.get());
        expense.setCategory(category.get());
        return expenseRepository.save(expense);
    }

    @GetMapping("/user/{userId}")
    public @ResponseBody List<Expense> getAllExpensesByUserId(@PathVariable Integer userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalStateException("User doesn't exist!");
        }
        return  expenseRepository.getByUserIdAndGroupIsNull(userId);
    }

//    @DeleteMapping
}
