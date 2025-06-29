package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionSchema, Long> {

    List<TransactionSchema> findAllByUserId(Long userId);

    List<TransactionSchema> findAllByAccountId(Long accountId);

    List<TransactionSchema> findAllByGoalId(Long goalId);

    List<TransactionSchema> findAllByUserIdAndTransferAccountId(Long userId, Long accountId);

}