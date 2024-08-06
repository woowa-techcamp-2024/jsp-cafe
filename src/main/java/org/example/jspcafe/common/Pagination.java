package org.example.jspcafe.common;

import lombok.Getter;

import java.util.List;

@Getter
public class Pagination<T> {
    private final List<T> items;
    private final int currentPage;
    private final int pageSize;
    private final long totalItems;
    private final int totalPages;

    public Pagination(List<T> items, int currentPage, int pageSize, long totalItems) {
        this.items = items;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }
}
