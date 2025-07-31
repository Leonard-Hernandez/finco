package com.finco.finco.entity.goal.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.finco.finco.entity.exception.AmountMustBeGreaterThanZeroException;
import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.entity.user.model.User;

public class Goal {

    private Long id;
    private User user;
    private String name;
    private BigDecimal targetAmount;
    private LocalDate deadLine;
    private String description;
    private LocalDateTime creationDate;
    private boolean enable;
    private List<GoalAccountBalance> goalAccountBalances;

    public Goal() {
    };

    public Goal(Long id, User user, String name, BigDecimal targetAmount, LocalDate deadLine, String description,
            LocalDateTime creationDate, boolean enable, List<GoalAccountBalance> goalAccountBalances) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.targetAmount = targetAmount;
        this.deadLine = deadLine;
        this.description = description;
        this.creationDate = creationDate;
        this.enable = enable;
        this.goalAccountBalances = goalAccountBalances;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        if (targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountMustBeGreaterThanZeroException();
        }
        this.targetAmount = targetAmount;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<GoalAccountBalance> getGoalAccountBalances() {
        return goalAccountBalances;
    }

    public void setGoalAccountBalances(List<GoalAccountBalance> goalAccountBalances) {
        this.goalAccountBalances = goalAccountBalances;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Goal other = (Goal) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Goal {id=" + id + ", user=" + user + ", name=" + name + ", targetAmount=" + targetAmount + ", deadLine="
                + deadLine + ", description=" + description + ", creationDate=" + creationDate + ", enable=" + enable
                + ", goalAccountBalances=" + goalAccountBalances + "}";
    }

}
