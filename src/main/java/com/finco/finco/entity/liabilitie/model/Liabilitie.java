package com.finco.finco.entity.liabilitie.model;

import java.time.LocalDate;

public class Liabilitie {

    private Long id;
    private Long userId;
    private Long name;
    private Long pendingBalance;
    private Long interestRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public Liabilitie() {

    }

    public Liabilitie(Long id, Long userId, Long name, Long pendingBalance, Long interestRate, LocalDate startDate,
            LocalDate endDate, String description) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.pendingBalance = pendingBalance;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public Long getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(Long pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public Long getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Long interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        Liabilitie other = (Liabilitie) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}
