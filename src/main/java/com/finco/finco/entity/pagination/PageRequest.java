package com.finco.finco.entity.pagination;

import java.util.Optional;

public class PageRequest {

    private final int pageNumber;
    private final int pageSize;
    private final String sortBy;
    private final String sortDirection;

    public PageRequest(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number must be non-negative.");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be positive.");
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Optional<String> getSortBy() {
        return Optional.ofNullable(sortBy);
    }

    public Optional<String> getSortDirection() {
        return Optional.ofNullable(sortDirection);
    }

}
