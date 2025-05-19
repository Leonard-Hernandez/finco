package com.finco.finco.infrastructure.config.db.schema;

import java.time.LocalDateTime;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.goalallocation.model.GoalAllocation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Accounts")
public class AccountSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserSchema user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(name = "balance", columnDefinition = "DEFAULT 0.00")
    private Long balance;

    @Column(name = "currency", 
                    columnDefinition = "Default 'COP'", 
                    length = 3)
    private CurrencyEnum currency;

    @Column(name = "creation_date", 
                    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    private String description;

    private boolean isDefault;

    // @OneToMany(mappedBy = "account")
    // private List<Transaction> transactions;

    // @OneToMany(mappedBy = "transferAccount")
    // private List<Transaction> transferTransactions;

    // @OneToMany(mappedBy = "sourceAccount")
    // private List<GoalAllocation> goalAllocations;

}
