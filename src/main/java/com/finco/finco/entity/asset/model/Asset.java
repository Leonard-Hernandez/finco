package com.finco.finco.entity.asset.model;

import java.time.LocalDate;

import com.finco.finco.entity.user.model.User;

public class Asset {

    private Long id;
    private User user;
    private String name;
    private Long estimatedValue;
    private LocalDate acquisitionDate;
    private Long interestRate;
    private String description;

    public Asset() {

    }

    public Asset(Long id, User user, String name, Long estimatedValue, LocalDate acquisitionDate,
            Long interestRate, String description) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.estimatedValue = estimatedValue;
        this.acquisitionDate = acquisitionDate;
        this.interestRate = interestRate;
        this.description = description;
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

    public Long getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(Long estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public Long getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Long interestRate) {
        this.interestRate = interestRate;
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
        Asset other = (Asset) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
