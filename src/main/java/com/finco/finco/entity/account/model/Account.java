package com.finco.finco.entity.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.exception.AmountMustBeGreaterThanZeroException;
import com.finco.finco.entity.exception.InsufficientBalanceException;
import com.finco.finco.entity.user.model.User;

public class Account {

    private Long id;
    private User user;
    private String name;
    private AccountType type;
    private BigDecimal balance;
    private CurrencyEnum currency;
    private LocalDateTime creationDate;
    private String description;
    private boolean isDefault;
    private boolean enable;
    private double depositFee;
    private double withdrawFee;

    public Account() {
    }

    public Account(Long id, User user, String name, AccountType type, BigDecimal balance, CurrencyEnum currency,
            LocalDateTime creationDate, String description, boolean isDefault, boolean enable, double depositFee,
            double withdrawFee) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.currency = currency;
        this.creationDate = creationDate;
        this.description = description;
        this.isDefault = isDefault;
        this.enable = enable;
        this.depositFee = depositFee;
        this.withdrawFee = withdrawFee;
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

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance != null ? balance : BigDecimal.ZERO;
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public double getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(double depositFee) {
        this.depositFee = depositFee;
    }

    public double getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(double withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public BigDecimal deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountMustBeGreaterThanZeroException();
        }
        BigDecimal fee = amount.multiply(new BigDecimal(this.depositFee));
        this.balance = this.balance.add(amount.subtract(fee));
        return this.balance;
    }

    public boolean hasSufficientBalance(BigDecimal amount) {
        return this.balance.compareTo(amount) >= 0;
    }

    public BigDecimal withdraw(BigDecimal amount) {
        BigDecimal fee = amount.multiply(new BigDecimal(this.withdrawFee));

        if (!hasSufficientBalance(amount.add(fee)) && this.type != AccountType.CREDIT) {
            throw new InsufficientBalanceException();
        }
        this.balance = this.balance.subtract(amount.add(fee));
        return this.balance;
    }

    public void transfer(BigDecimal amount, Account transferAccount) {
        this.withdraw(amount);
        BigDecimal fee = amount.multiply(new BigDecimal(this.withdrawFee));
        transferAccount.deposit(amount.subtract(fee));
    }

    public void transfer(BigDecimal amount, Account transferAccount, BigDecimal exchageRate) {
        this.withdraw(amount);
        BigDecimal fee = amount.multiply(new BigDecimal(this.withdrawFee));
        BigDecimal amountToTransfer = amount.subtract(fee);
        transferAccount.deposit(amountToTransfer.multiply(exchageRate));
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
        Account other = (Account) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Account {id=" + id + ", user=" + user + ", name=" + name + ", type=" + type + ", balance=" + balance
                + ", currency=" + currency + ", creationDate=" + creationDate + ", description=" + description
                + ", isDefault=" + isDefault + ", enable=" + enable + ", depositFee=" + depositFee + ", withdrawFee="
                + withdrawFee + "}";
    }

}
