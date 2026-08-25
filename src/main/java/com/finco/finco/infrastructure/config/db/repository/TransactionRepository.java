package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;

public interface TransactionRepository extends JpaRepository<TransactionSchema, Long>, JpaSpecificationExecutor<TransactionSchema> {

    @NonNull
    @Override
    Page<TransactionSchema> findAll(@Nullable Specification<TransactionSchema> specification, @Nullable Pageable pageable);

    @Query("SELECT DISTINCT t.category FROM TransactionSchema t join AccountSchema a on t.account.id = a.id WHERE t.user.id = :userId and a.enable = true")
    List<String> findAllCategoriesByUserId(Long userId);

}