package com.finco.finco.infrastructure.config.db.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.finco.finco.entity.pagination.filter.IAccountFilterData;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;

import jakarta.persistence.criteria.Predicate;

public class AccountSchemeSpecification {

    public static Specification<AccountSchema> getSpecification(IAccountFilterData filterData) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filterData.currency() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currency"),filterData.currency()));
            }

            if (filterData.type() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"),filterData.type()));
            }

            if (filterData.userId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), filterData.userId()));
            }

            if (filterData.enable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("enable"), filterData.enable()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

}
