package com.finco.finco.infrastructure.config.db.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.asset.model.Asset;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.liabilitie.model.Liabilitie;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Component
public class UserMapper {

    private final AccountMapper accountMapper;
    private final GoalMapper goalMapper;
    private final AssetMapper assetMapper;
    private final LiabilitieMapper liabilitieMapper;
    private final RoleMapper roleMapper;
    private final TransactionMapper transactionMapper;

    public UserMapper(AccountMapper accountMapper, GoalMapper GoalMapper, AssetMapper assetMapper,
            LiabilitieMapper liabilitieMapper, RoleMapper roleMapper, TransactionMapper transactionMapper) {
        this.accountMapper = accountMapper;
        this.goalMapper = GoalMapper;
        this.assetMapper = assetMapper;
        this.liabilitieMapper = liabilitieMapper;
        this.roleMapper = roleMapper;
        this.transactionMapper = transactionMapper;
    }

    public User toUser(UserSchema userSchema) {
        if (userSchema == null) {
            return null;
        }

        User user = new User();

        user.setId(userSchema.getId());
        user.setName(userSchema.getName());
        user.setEmail(userSchema.getEmail());
        user.setPassword(userSchema.getPassword());
        user.setRegistrationDate(userSchema.getRegistrationDate());
        user.setEnable(userSchema.getEnable());

        List<Account> accounts = userSchema.getAccounts() != null
                ? userSchema.getAccounts().stream().map(accountMapper::toAccount).collect(Collectors.toList())
                : List.of();

        List<Goal> goals = userSchema.getGoals() != null
                ? userSchema.getGoals().stream().map(goalMapper::toGoal).collect(Collectors.toList())
                : List.of();

        List<Asset> assets = userSchema.getAssets() != null
                ? userSchema.getAssets().stream().map(assetMapper::toAsset).collect(Collectors.toList())
                : List.of();

        List<Liabilitie> liabilities = userSchema.getLiabilities() != null
                ? userSchema.getLiabilities().stream().map(liabilitieMapper::toLiabilitie).collect(Collectors.toList())
                : List.of();

        List<Role> roles = userSchema.getRoles() != null
                ? userSchema.getRoles().stream().map(roleMapper::toRole).collect(Collectors.toList())
                : List.of();

        List<Transaction> transactions = userSchema.getTransactions() != null
                ? userSchema.getTransactions().stream().map(transactionMapper::toTransaction)
                        .collect(Collectors.toList())
                : List.of();

        user.setAccounts(accounts);
        user.setGoals(goals);
        user.setAssets(assets);
        user.setLiabilities(liabilities);
        user.setRoles(roles);
        user.setTransactions(transactions);

        return user;

    }

    public User toLigthUser(UserSchema userSchema) {
        if (userSchema == null) {
            return null;
        }

        User user = new User();

        user.setId(userSchema.getId());
        user.setName(userSchema.getName());
        user.setEmail(userSchema.getEmail());
        user.setPassword(userSchema.getPassword());
        user.setRegistrationDate(userSchema.getRegistrationDate());
        user.setEnable(userSchema.getEnable());

        return user;
    }

}
