package com.example.expensetracker.controller;
import com.example.expensetracker.dto.GroupCreateRequest;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.Group;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.ICategoryRepository;
import com.example.expensetracker.repository.IExpenseRepository;
import com.example.expensetracker.repository.IGroupRepository;
import com.example.expensetracker.repository.IUserRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/group")
@PermitAll
public class GroupController {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IGroupRepository groupRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IExpenseRepository expenseRepository;

    @PostMapping("/create")
    public @ResponseBody int create(@RequestBody GroupCreateRequest request){
        Optional<User> _creator = userRepository.findById(request.getCreatorId());
        System.out.println(request.getCreatorId() + request.getGroup().getName());
        if(_creator.isEmpty()){
            throw new IllegalStateException("User doesn't exist!");
        }
        List<Integer> memberIds = request.getMembers();
        User creator = _creator.get();
        Group group = request.getGroup();
        group.setLeader(creator);
        Group savedGroup = groupRepository.save(group);
        creator.setGroup(savedGroup);
        if(!memberIds.isEmpty()) {
            groupRepository.addUsersToGroup(savedGroup, memberIds);
        }
        userRepository.save(creator);
        return savedGroup.getId();
    }

    @GetMapping("/details/{id}")
    public @ResponseBody Group getDetails(@PathVariable Integer id){
        Optional<Group> group = groupRepository.getMembersById(id);
        if(group.isEmpty()){
            throw new IllegalStateException("Group doesn't exist!");
        }
        return group.get();
    }
    @PostMapping("/add/{groupId}/{userId}")
    public @ResponseBody Group addMember(@PathVariable Integer groupId, @PathVariable Integer userId, @RequestParam Integer selfId){
        Optional<User> _user = userRepository.findById(userId);
        Optional<Group> _group = groupRepository.findById(groupId);
        Optional<User> _self = userRepository.findById(selfId);
        if(_self.isEmpty()|| _user.isEmpty() || _group.isEmpty()){
            throw new IllegalStateException("Invalid Request Params!");
        }
        Group group = _group.get();
        User user = _user.get();
        User self = _self.get();
        if (group.getLeader().equals(self)) {
            List<User> members = group.getMembers();
            members.add(user);
            group.setMembers(members);
            user.setGroup(group);
            groupRepository.save(group);
            userRepository.save(user);
        }
        return group;
    }
    @PostMapping("/remove/{groupId}/{userId}")
    public @ResponseBody Group removeMember(@PathVariable Integer groupId, @PathVariable Integer userId, @RequestParam Integer selfId){
        Optional<User> _user = userRepository.findById(userId);
        Optional<Group> _group = groupRepository.findById(groupId);
        Optional<User> _self = userRepository.findById(selfId);
        if(_self.isEmpty()|| _user.isEmpty() || _group.isEmpty()){
            throw new IllegalStateException("Invalid Request Params!");
        }
        Group group = _group.get();
        User user = _user.get();
        User self = _self.get();
        List<User> members = group.getMembers();

        if(!members.contains(user)){
            throw new IllegalStateException("User isn't a part of this group!");
        }
        if (group.getLeader().equals(self) || self.equals(user)) {
            members.remove(user);
            group.setMembers(members);
            user.setGroup(null);
            groupRepository.save(group);
            userRepository.save(user);
        }
        return group;
    }


    @PostMapping("/expense/add")
    public @ResponseBody Expense addExpense(@RequestBody Expense expense, @RequestParam Integer groupId, @RequestParam Integer userId, @RequestParam Integer categoryId){
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<User> user = userRepository.findById(userId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(user.isEmpty() || group.isEmpty() || category.isEmpty()){
            throw new IllegalStateException("Missing/Invalid Request Params");
        }
        expense.setUser(user.get());
        expense.setCategory(category.get());
        expense.setGroup(group.get());
        return expenseRepository.save(expense);
    }

    @GetMapping("/expense/{groupId}")
    public @ResponseBody List<Expense> getAllExpenses(@PathVariable Integer groupId){
        Optional<Group> group = groupRepository.findById(groupId);
        if(group.isEmpty()){
            throw new IllegalStateException("Group doesn't exist!");
        }
        return expenseRepository.getExpenseByGroupId(groupId);
    }

}
