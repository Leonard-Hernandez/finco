package com.finco.finco.infrastructure.config.db.schema;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "users")
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

    @Column(name = "enable", columnDefinition = "DEFAULT 1")
    private Boolean enable;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Users_Roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = {
            @UniqueConstraint(columnNames = {
                    "user_id", "role_id"
            }) })
    private List<RoleSchema> roles;

}
