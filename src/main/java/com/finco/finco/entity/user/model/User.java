package com.finco.finco.entity.user.model;

import java.time.LocalDateTime;
import java.util.List;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.asset.model.Asset;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.liabilitie.model.Liabilitie;
import com.finco.finco.entity.role.model.Role;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime registrationDate;

    private List<Account> accounts;
    private List<Goal> goals;
    private List<Asset> assets;
    private List<Liabilitie> liabilities;
    private List<Role> roles;

    public User() {

    }

    public User(Long id, String name, String email, String password, LocalDateTime registrationDate,
            List<Account> accounts, List<Goal> goals, List<Asset> assets, List<Liabilitie> liabilities,
            List<Role> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.accounts = accounts;
        this.goals = goals;
        this.assets = assets;
        this.liabilities = liabilities;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public List<Liabilitie> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(List<Liabilitie> liabilities) {
        this.liabilities = liabilities;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
