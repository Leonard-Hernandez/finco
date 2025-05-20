package com.finco.finco.infrastructure.config.db.schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.asset.model.Asset;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.liabilitie.model.Liabilitie;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Users")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registration_Date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "user")
    private List<AccountSchema> accounts;

    @OneToMany(mappedBy = "user")
    private List<GoalSchema> goals;

    @OneToMany(mappedBy = "user")
    private List<TransactionSchema> transactions;

    @OneToMany(mappedBy = "user")
    private List<AssetSchema> assets;

    @OneToMany(mappedBy = "user")
    private List<LiabilitieSchema> liabilities;

    @ManyToMany
    @JoinTable(name = "Users_Roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = {
            @UniqueConstraint(columnNames = {
                    "user_id", "role_id"
            }) })
    private List<RoleSchema> roles;

    public User toUser() {
        List<Account> accounts = this.getAccounts().stream().map(AccountSchema::toAccount).collect(Collectors.toList());
        List<Goal> goals = this.getGoals().stream().map(GoalSchema::toGoal).collect(Collectors.toList());
        List<Asset> assets = this.getAssets().stream().map(AssetSchema::toAsset).collect(Collectors.toList());
        List<Liabilitie> liabilities = this.getLiabilities().stream().map(LiabilitieSchema::toLiabilitie).collect(Collectors.toList());
        List<Role> roles = this.getRoles().stream().map(RoleSchema::toRole).collect(Collectors.toList());
        List<Transaction> transactions = this.getTransactions().stream().map(TransactionSchema::toTransaction).collect(Collectors.toList());
        User user = new User(
            this.getId(),
            this.getName(),
            this.getEmail(),
            this.getPassword(),
            this.getRegistrationDate(),
            accounts,
            goals,
            assets,
            liabilities,
            roles,
            transactions
        );

        return user;
    }
}
