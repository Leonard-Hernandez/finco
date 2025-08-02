package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.pagination.SimplePage;

public class PageMapper {

    public static PageRequest toPageRequest(Pageable pageable) {
        return new PageRequest(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort().isSorted() ? pageable.getSort().iterator().next().getProperty() : null,
            pageable.getSort().isSorted() ? pageable.getSort().iterator().next().getDirection().name().toLowerCase() : null
        );
    }

    public static PageRequest toPageRequest(Integer Page, Integer size, String sortBy, String sortDirection) {
        return new PageRequest(
            Page,
            size,
            sortBy,
            sortDirection
        );
    }

    public static Pageable toPageable(PageRequest pageRequest) {
        Sort sort = pageRequest.getSortBy()
        .map(sortBy -> {
            Sort.Direction direction = pageRequest.getSortDirection()
                    .filter(d -> d.equalsIgnoreCase("desc"))
                    .map(d -> Sort.Direction.DESC)
                    .orElse(Sort.Direction.ASC);
            return Sort.by(direction, sortBy);
        })
        .orElse(Sort.unsorted());

        return org.springframework.data.domain.PageRequest.of(
            pageRequest.getPageNumber(),
            pageRequest.getPageSize(),
            sort);
    }

    public static <T> SimplePage<T> toPage(PagedResult<T> pageResult, Pageable pageable) {
        return new SimplePage<>(pageResult.getContent(), pageable, pageResult.getTotalElements());
    }

}
