package com.finco.finco.infrastructure.config.db.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.asset.model.Asset;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.liabilitie.model.Liabilitie;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;
import com.finco.finco.infrastructure.config.db.schema.AssetSchema;
import com.finco.finco.infrastructure.config.db.schema.GoalSchema;
import com.finco.finco.infrastructure.config.db.schema.LiabilitieSchema;
import com.finco.finco.infrastructure.config.db.schema.RoleSchema;
import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;
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

    public UserSchema toUserSchema(User user) {
        if (user == null) {
            return null;
        }

        UserSchema userSchema = new UserSchema();

        userSchema.setId(user.getId());
        userSchema.setName(user.getName());
        userSchema.setEmail(user.getEmail());
        userSchema.setPassword(user.getPassword());
        userSchema.setRegistrationDate(user.getRegistrationDate());
        userSchema.setEnable(user.getEnable());

        List<AccountSchema> accounts = user.getAccounts() != null
                ? user.getAccounts().stream().map(accountMapper::toAccountSchema).collect(Collectors.toList())
                : List.of();

        List<GoalSchema> goals = user.getGoals() != null
                ? user.getGoals().stream().map(goalMapper::toGoalSchema).collect(Collectors.toList())
                : List.of();

        List<AssetSchema> assets = user.getAssets() != null
                ? user.getAssets().stream().map(assetMapper::toAssetSchema).collect(Collectors.toList())
                : List.of();

        List<LiabilitieSchema> liabilities = user.getLiabilities() != null
                ? user.getLiabilities().stream().map(liabilitieMapper::toLiabilitieSchema).collect(Collectors.toList())
                : List.of();

        List<RoleSchema> roles = user.getRoles() != null
                ? user.getRoles().stream().map(roleMapper::toRoleSchema).collect(Collectors.toList())
                : List.of();

        List<TransactionSchema> transactions = user.getTransactions() != null
                ? user.getTransactions().stream().map(transactionMapper::toTransactionSchema)
                        .collect(Collectors.toList())
                : List.of();

        userSchema.setAccounts(accounts);
        userSchema.setGoals(goals);
        userSchema.setAssets(assets);
        userSchema.setLiabilities(liabilities);
        userSchema.setRoles(roles);
        userSchema.setTransactions(transactions);

        return userSchema;
    }

}
