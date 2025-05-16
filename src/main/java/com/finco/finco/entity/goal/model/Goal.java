package com.finco.finco.entity.goal.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Goal {

    private Long id;
    private Long userId;
    private String name;
    private Long targetAmount;
    private LocalDate deadLine;
    private String description;
    private LocalDateTime creationDate;
    private Long savedAmount;

    public Goal() {
    };

    public Goal(Long id, Long userId, String name, Long targetAmount, LocalDate deadLine, String description,
            LocalDateTime creationDate, Long savedAmount) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.deadLine = deadLine;
        this.description = description;
        this.creationDate = creationDate;
        this.savedAmount = savedAmount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Long targetAmount) {
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

    public Long getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(Long savedAmount) {
        this.savedAmount = savedAmount;
    }

}
