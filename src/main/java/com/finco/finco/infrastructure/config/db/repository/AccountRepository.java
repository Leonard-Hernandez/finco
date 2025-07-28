package com.finco.finco.infrastructure.config.db.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.AccountSchema;

@Repository
public interface AccountRepository extends JpaRepository<AccountSchema,Long> {

    Page<AccountSchema> findAllByUserIdAndEnableTrue(Pageable pageable, Long userId);

    @Query("SELECT SUM(a.balance) as total FROM AccountSchema a WHERE a.user.id = :userId and a.enable = true and a.type != 'CREDIT'")
    Long getTotalByUserId(Long userId);

    @Query("SELECT a.version FROM AccountSchema a WHERE a.id = :id")
    Long getVersionByAccountId(Long id);

    Page<AccountSchema> findAllByEnableTrue(Pageable springPageable);

    Optional<AccountSchema> findByIsDefaultTrueAndUserId(Long id);

    List<AccountSchema> findAllByUserId(Long userId);

    @Query("SELECT SUM(a.balance) as total FROM GoalAccountBalanceSchema a WHERE a.account.id = :accountId")
    BigDecimal getTotalBalanceInGoalsByAccount(Long accountId);

}
