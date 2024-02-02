package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IGroupRepository extends CrudRepository<Group, Integer> {
    Optional<Group> getMembersById(int groupId);

    @Query("SELECT ug.group FROM UserGroup ug WHERE ug.user.id = :userId")
    List<Group> findGroupsByUserId(@Param("userId") Integer userId);

}
