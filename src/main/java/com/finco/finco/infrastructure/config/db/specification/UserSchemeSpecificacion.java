package com.finco.finco.infrastructure.config.db.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.finco.finco.entity.pagination.filter.IUserFilterData;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

import jakarta.persistence.criteria.Predicate;

public class UserSchemeSpecificacion {

    public static Specification<UserSchema> getSpecification(IUserFilterData filterData) {
        return (var root, var query, var criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filterData.userId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), filterData.userId()));
            }

            if (filterData.email() != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"), filterData.email()));
            }

            if (filterData.name() != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"), filterData.name()));
            }

            if (filterData.enable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("enable"), filterData.enable()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));

        };
    }

}
