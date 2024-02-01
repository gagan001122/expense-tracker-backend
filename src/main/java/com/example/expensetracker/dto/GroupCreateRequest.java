package com.example.expensetracker.dto;

import com.example.expensetracker.entity.Group;

import java.util.List;

public class GroupCreateRequest {
    private Group group;
    private int creatorId;
    private List<Integer> members;
    public Group getGroup() {
        return group;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public int getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
    public List<Integer> getMembers() {
        return members;
    }
    public void setMembers(List<Integer> members) {
        this.members = members;
    }
}
