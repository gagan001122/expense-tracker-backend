package com.example.expensetracker.repository;

import com.example.expensetracker.entity.UserGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserGroupRepository extends CrudRepository<UserGroup, Integer> {
    Optional<UserGroup> getByUserId(int userId);
}
