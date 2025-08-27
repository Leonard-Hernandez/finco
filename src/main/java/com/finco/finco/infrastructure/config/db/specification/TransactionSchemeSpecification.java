package com.finco.finco.infrastructure.config.db.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.finco.finco.entity.pagination.filter.ITransactionFilterData;
import com.finco.finco.entity.transaction.model.TransactionType;
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
                predicates.add(
                        criteriaBuilder.equal(root.get("transferAccount").get("id"), filterData.transferAccountId()));
            }

            if (filterData.category() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), filterData.category()));
            }

            if (filterData.type() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filterData.type()));
            } else if (filterData.onlyAccountTransactions() != null) {
                predicates.add(root.get("type").in(TransactionType.DEPOSIT, TransactionType.WITHDRAW));
            } else if (filterData.onlyGoalTransactions() != null) {
                predicates.add(root.get("type").in(TransactionType.DEPOSIT_GOAL, TransactionType.WITHDRAW_GOAL));
            }

            if (filterData.startDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filterData.startDate()));
            }

            if (filterData.endDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filterData.endDate()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));

        };
    }

}
