package com.finco.finco.entity.pagination;

import java.util.Collections;
import java.util.List;

public class PagedResult<T> {

    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int pageNumber;
    private final int pageSize;
    private final boolean isFirst;
    private final boolean isLast;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public PagedResult(List<T> content, long totalElements, int totalPages,
                       int pageNumber, int pageSize, boolean isFirst,
                       boolean isLast, boolean hasNext, boolean hasPrevious) {
        this.content = Collections.unmodifiableList(content);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public static <T> PagedResult<T> empty(PageRequest pageRequest) {
        return new PagedResult<>(
            Collections.emptyList(), 0, 0,
            pageRequest.getPageNumber(), pageRequest.getPageSize(),
            true, true, false, false
        );
    }

}
