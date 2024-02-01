package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends CrudRepository<Category, Integer> {
}
