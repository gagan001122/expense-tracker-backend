package com.example.expensetracker.controller;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.repository.ICategoryRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@PermitAll
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private ICategoryRepository categoryRepository;

    @PostMapping("/create")
    public @ResponseBody Category create(@RequestBody Category category){
        return categoryRepository.save(category);
    }

    @GetMapping("/all")
    public @ResponseBody List<Category> getAll(){
        return (List<Category>) categoryRepository.findAll();
    }
}
