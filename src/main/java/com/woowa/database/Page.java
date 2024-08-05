package com.woowa.database;

import java.util.List;
import java.util.Objects;

public class Page<T> {
    private final List<T> content;
    private final Long totalElements;
    private final int totalPages;
    private final int page;
    private final int size;

    public Page(List<T> content, Long totalElements, int totalPages, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
    }

    public static <T> Page<T> of(List<T> content, Long count, int page, int size) {
        int totalPages = (int) Math.ceil((double) count / size);
        return new Page<>(content, count, totalPages, page, size);
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
        Page<?> page1 = (Page<?>) o;
        return totalPages == page1.totalPages && page == page1.page && size == page1.size && Objects.equals(
                content, page1.content) && Objects.equals(totalElements, page1.totalElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, totalElements, totalPages, page, size);
    }

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
