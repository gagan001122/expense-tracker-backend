package com.example.expensetracker.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private User leader;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<User> members;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}

