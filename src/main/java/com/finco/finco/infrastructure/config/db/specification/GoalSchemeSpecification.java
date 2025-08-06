package com.finco.finco.infrastructure.config.db.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.finco.finco.entity.pagination.filter.IGoalFilterData;
import com.finco.finco.infrastructure.config.db.schema.GoalAccountBalanceSchema;
import com.finco.finco.infrastructure.config.db.schema.GoalSchema;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class GoalSchemeSpecification {

    public static Specification<GoalSchema> getSpecification(IGoalFilterData filterData) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            
            if (filterData.userId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), filterData.userId()));
            }

            if (filterData.accountId() != null) {

                Subquery<Long> subquery = query.subquery(Long.class);
                Root<GoalAccountBalanceSchema> subqueryRoot = subquery.from(GoalAccountBalanceSchema.class);
                subquery.select(subqueryRoot.get("goal").get("id"));
                subquery.where(criteriaBuilder.equal(subqueryRoot.get("account").get("id"), filterData.accountId()));
                predicates.add(root.get("id").in(subquery));

            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));

        };
    }

}
