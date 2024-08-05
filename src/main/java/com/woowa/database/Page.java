package com.woowa.database;

import java.util.List;
import java.util.Objects;

public class Page<T> {
    private final List<T> content;
    private final Long totalElements;
    private final int totalPages;
    private final int startPage;
    private final int endPage;
    private final int page;
    private final int size;

    public Page(List<T> content, Long totalElements, int totalPages, int startPage, int endPage, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.startPage = startPage;
        this.endPage = endPage;
        this.page = page;
        this.size = size;
    }

    public static <T> Page<T> of(List<T> content, Long count, int page, int size) {
        int totalPages = (int) Math.ceil((double) count / size);
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(startPage + 4, totalPages - 1);
        return new Page<>(content, count, totalPages, startPage, endPage, page, size);
    }

    public List<T> getContent() {
        return content;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Page<?> page = (Page<?>) o;
        return totalPages == page.totalPages && startPage == page.startPage && endPage == page.endPage
                && Objects.equals(content, page.content) && Objects.equals(totalElements,
                page.totalElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, totalElements, totalPages, startPage, endPage);
    }

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", startPage=" + startPage +
                ", endPage=" + endPage +
                '}';
    }
}
