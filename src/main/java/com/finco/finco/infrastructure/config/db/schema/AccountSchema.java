package com.finco.finco.infrastructure.config.db.schema;

import java.time.LocalDateTime;
import java.util.List;

import com.finco.finco.entity.account.model.Account;
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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Accounts")
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

    @OneToMany(mappedBy = "account")
     private List<TransactionSchema> transactions;

    @OneToMany(mappedBy = "account")
    private List<TransactionSchema> transferTransactions;

    public Account toAccount() {
        Account account = new Account(
            this.getId(),
            this.getUser().getId(),
            this.getName(),
            this.getType(),
            this.getBalance(),
            this.getCurrency(),
            this.getCreationDate(),
            this.getDescription(),
            this.isDefault()
        );
        return account;
    }

}
