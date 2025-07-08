package com.finco.finco.infrastructure.config.db.schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;

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
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "balance", precision = 19, scale = 2, columnDefinition = "DECIMAL(19,2) DEFAULT 0.00")
    private BigDecimal balance;

    @Column(name = "currency", 
                    columnDefinition = "Default 'COP'", 
                    length = 3)
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;

    @Column(name = "creation_date", 
                    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    private String description;

    private boolean isDefault;

    @Column(name = "enable", columnDefinition = "DEFAULT 1")
    private boolean enable;

    @OneToMany(mappedBy = "account")
     private List<TransactionSchema> transactions;

    @OneToMany(mappedBy = "account")
    private List<TransactionSchema> transferTransactions;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "deposit_fee", columnDefinition = "DEFAULT 0.00")
    private double depositFee;

    @Column(name = "withdraw_fee", columnDefinition = "DEFAULT 0.00")
    private double withdrawFee;

    @OneToMany(mappedBy = "account")
    private List<GoalAccountBalanceSchema> goalAccountBalances;

}
