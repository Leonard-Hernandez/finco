package com.finco.finco.entity.goalallocation.model;

import java.time.LocalDateTime;

public class GoalAllocation {

    private Long id;
    private Long userId;
    private Long goalId;
    private Long sourceAccountId;
    private Long allocatedAmount;
    private LocalDateTime allocationDate;
    private String description;

    public GoalAllocation() {
    }
 
    public GoalAllocation(Long id, Long userId, Long goalId, Long sourceAccountId, Long allocatedAmount,
            LocalDateTime allocationDate, String description) {
        this.id = id;
        this.userId = userId;
        this.goalId = goalId;
        this.sourceAccountId = sourceAccountId;
        this.allocatedAmount = allocatedAmount;
        this.allocationDate = allocationDate;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Long getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(Long allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public LocalDateTime getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(LocalDateTime allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
