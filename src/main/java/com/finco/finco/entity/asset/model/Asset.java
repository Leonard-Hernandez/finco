package com.finco.finco.entity.asset.model;

import java.time.LocalDate;

public class Asset {

    private Long id;
    private Long userId;
    private String name;
    private Long estimatedValue;
    private LocalDate acquisitionDate;
    private Long interestRate;
    private String description;

    public Asset() {

    }

    public Asset(Long id, Long userId, String name, Long estimatedValue, LocalDate acquisitionDate,
            String description) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.estimatedValue = estimatedValue;
        this.acquisitionDate = acquisitionDate;
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

}
