package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionSchema, Long> {

    Page<TransactionSchema> findAllByUserId(Long userId, Pageable pageable);

    Page<TransactionSchema> findAllByAccountId(Long accountId, Pageable pageable);

    Page<TransactionSchema> findAllByGoalId(Long goalId, Pageable pageable);

    Page<TransactionSchema> findAllByUserIdAndTransferAccountId(Long userId, Long accountId, Pageable pageable);

    @Query("SELECT DISTINCT t.category FROM TransactionSchema t WHERE t.user.id = :userId and t.category is not null")
    List<String> findAllCategoriesByUserId(Long userId);

}