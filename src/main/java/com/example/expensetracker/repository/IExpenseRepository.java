package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExpenseRepository extends CrudRepository<Expense, Integer> {
    List<Expense> getExpenseByUserId(int i);
}
