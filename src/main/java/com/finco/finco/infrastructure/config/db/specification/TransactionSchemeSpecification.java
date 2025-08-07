package com.finco.finco.infrastructure.config.db.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.finco.finco.entity.pagination.filter.ITransactionFilterData;
import com.finco.finco.infrastructure.config.db.schema.TransactionSchema;

import jakarta.persistence.criteria.Predicate;

public class TransactionSchemeSpecification {

     public static Specification<TransactionSchema> getSpecification(ITransactionFilterData filterData) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            
            if (filterData.userId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), filterData.userId()));
            }

            if (filterData.accountId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("account").get("id"), filterData.accountId()));
                
            }

            if (filterData.goalId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("goal").get("id"), filterData.goalId()));                
            }

            if (filterData.transferAccountId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("transferAccount").get("id"), filterData.transferAccountId()));                
            }

            if (filterData.category() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), filterData.category()));                
            }

            if (filterData.type() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filterData.type()));                
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));

        };
    }

}
