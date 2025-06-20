package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;

@Repository
public interface AccountRepository extends JpaRepository<AccountSchema,Long> {

    List<AccountSchema> findByUserId(Long userId);

    List<AccountSchema> findByUserIdAndType(Long userId , AccountType type);

    @Query("SELECT SUM(a.balance) FROM AccountSchema a WHERE a.user.id = :userId")
    Long getTotalByUserId(Long userId);

    @Query("SELECT a.version FROM AccountSchema a WHERE a.id = :id")
    Long getVersionByAccountId(Long id);

}
