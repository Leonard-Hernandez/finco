package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionSchema, Long> {

    List<TransactionSchema> findByUserId(Long userId);

    List<TransactionSchema> findByAccountId(Long accountId);

    List<TransactionSchema> findByUserIdAndType(Long userId, TransactionType type);

    List<TransactionSchema> findByUserIdAndCategoryLike(Long userId, String search);

    List<TransactionSchema> findByGoalId(Long goalId);

    List<TransactionSchema> findByUserIdAndTransferAccountId(Long userId, Long accountId);

}