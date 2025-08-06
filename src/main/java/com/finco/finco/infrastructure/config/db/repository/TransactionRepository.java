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
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionSchema, Long>, JpaSpecificationExecutor<TransactionSchema> {

    @NonNull
    @Override
    Page<TransactionSchema> findAll(@Nullable Specification<TransactionSchema> specification, @Nullable Pageable pageable);

    @Query("SELECT DISTINCT t.category FROM TransactionSchema t WHERE t.user.id = :userId and t.category is not null")
    List<String> findAllCategoriesByUserId(Long userId);

}