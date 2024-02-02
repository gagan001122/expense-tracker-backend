package com.example.expensetracker.controller;
import com.example.expensetracker.dto.GroupCreateRequest;
import com.example.expensetracker.entity.*;
import com.example.expensetracker.repository.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@CrossOrigin(origins = "*")
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

    @Autowired
    private IUserGroupRepository userGroupRepository;

    private UserGroup createGroupMember(int userId, Group group, boolean isLeader){
        Optional<User> _user = userRepository.findById(userId);
        if(_user.isEmpty()){
            throw new IllegalStateException("User doesn't exist!");
        }
        UserGroup userGroup = new UserGroup();
        userGroup.setUser(_user.get());
        userGroup.setLeader(isLeader);
        userGroup.setGroup(group);
        return userGroupRepository.save(userGroup);
    }

    @GetMapping("/user/{id}")
    public @ResponseBody List<Group> getGroupsByUser(@PathVariable Integer id){
        return groupRepository.findGroupsByUserId(id);
    }

    @PostMapping("/create")
    public @ResponseBody int create(@RequestBody GroupCreateRequest request){
        Group group = groupRepository.save(request.getGroup());
        UserGroup leader = createGroupMember(request.getCreatorId(), group, true);
        List<Integer> memberIds = request.getMembers();
        for(int id : memberIds){
            createGroupMember(id, group, false);
        }
        return group.getId();
    }

    @GetMapping("/details/{id}")
    public @ResponseBody Group getDetails(@PathVariable Integer id) {
        Optional<Group> group = groupRepository.getMembersById(id);
        if(group.isEmpty()){
            throw new IllegalStateException("Group doesn't exist!");
        }
        return group.get();
    }
    @PostMapping("/add/{groupId}/{userId}")
    public @ResponseBody Group addMember(@PathVariable Integer groupId, @PathVariable Integer userId, @RequestParam Integer selfId){
        Optional<Group> _group = groupRepository.findById(groupId);
        Optional<User> _self = userRepository.findById(selfId);
        if(_self.isEmpty()||_group.isEmpty()){
            throw new IllegalStateException("Invalid Request Params!");
        }
        createGroupMember(userId, _group.get(), false);
        return _group.get();
    }
    @PostMapping("/remove/{groupId}/{userId}")
    public @ResponseBody Group removeMember(@PathVariable Integer groupId, @PathVariable Integer userId, @RequestParam Integer selfId){
        Optional<User> _user = userRepository.findById(userId);
        Optional<Group> _group = groupRepository.findById(groupId);
        Optional<User> _self = userRepository.findById(selfId);
        if(_self.isEmpty()|| _user.isEmpty() || _group.isEmpty()){
            throw new IllegalStateException("Invalid Request Params!");
        }
        Optional<UserGroup> _ug = userGroupRepository.getByUserId(_user.get().getId());
        if(_ug.isEmpty()){
            throw new IllegalStateException("Group relation doesn't exist");
        }
        if(!_ug.get().getUser().equals(_self.get()) && !_ug.get().getUser().equals(_user.get())){
            throw new IllegalStateException("Not Authorized!");
        }
        userGroupRepository.delete(_ug.get());
        return _group.get();
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
