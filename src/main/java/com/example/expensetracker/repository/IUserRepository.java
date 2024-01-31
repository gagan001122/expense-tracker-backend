package com.example.expensetracker.repository;
import java.util.Optional;
import com.example.expensetracker.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email=?1")
    Optional<User> getUserByEmail(String email);
}
