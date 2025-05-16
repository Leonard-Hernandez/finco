package com.finco.finco.entity.account.model;

import java.time.LocalDateTime;

public class Account {

    private Long id;
    private Long userId;
    private String name;
    private TypeAccount type;
    private Long balance;
    private CurrencyEnum currency;
    private LocalDateTime creationDate;
    private String description;
    private boolean isDefault;

    public Account() {
    }

    public Account(Long id, Long userId, String name, TypeAccount type, Long balance, CurrencyEnum currency,
            LocalDateTime creationDate, String description, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.currency = currency;
        this.creationDate = creationDate;
        this.description = description;
        this.isDefault = isDefault;
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

    public TypeAccount getType() {
        return type;
    }

    public void setType(TypeAccount type) {
        this.type = type;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}
