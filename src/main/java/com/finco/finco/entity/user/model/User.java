package com.finco.finco.entity.user.model;

import java.time.LocalDateTime;
import java.util.List;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.asset.model.Asset;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.liabilitie.model.Liabilitie;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.entity.transaction.model.Transaction;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private CurrencyEnum defaultCurrency;
    private Boolean enable;

    private List<Account> accounts;
    private List<Goal> goals;
    private List<Asset> assets;
    private List<Liabilitie> liabilities;
    private List<Role> roles;
    private List<Transaction> transactions;

    public User() {

    }

    public User(Long id, String name, String email, String password, LocalDateTime registrationDate,
            CurrencyEnum defaultCurrency, Boolean enable, List<Account> accounts, List<Goal> goals, List<Asset> assets, 
            List<Liabilitie> liabilities, List<Role> roles, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.defaultCurrency = defaultCurrency;
        this.enable = enable;
        this.accounts = accounts;
        this.goals = goals;
        this.assets = assets;
        this.liabilities = liabilities;
        this.roles = roles;
        this.transactions = transactions;
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

    public CurrencyEnum getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(CurrencyEnum defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    } 

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
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
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User {id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
                + ", registrationDate=" + registrationDate + ", enable=" + enable + "}";
    }

}
