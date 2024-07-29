package com.codesquad.cafe.db;

import java.util.Collections;
import java.util.List;

public class Page<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private int actualSize;

    private long totalElements;

    private int totalPages;

    private boolean isFirstPage;

    private boolean isLastPage;

    private Page(List<T> content,
                 int pageNumber,
                 int pageSize,
                 int actualSize,
                 long totalElements,
                 int totalPages,
                 boolean isFirstPage,
                 boolean isLastPage) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("page number should be greater than 0");
        }
        if (pageNumber > totalPages) {
            throw new IllegalArgumentException("page number should be less than or equal to total pages");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("page size should be greater than 0");
        }
        if (actualSize > pageSize) {
            throw new IllegalArgumentException("actual size should be less than or equal to page size");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("total elements should be greater than or equal to 0");
        }
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.actualSize = actualSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isFirstPage = isFirstPage;
        this.isLastPage = isLastPage;
    }

    public static <T> Page<T> of(
            List<T> content,
            int pageNumber,
            int pageSize,
            long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        // 없는 페이지 요청시 예외 처리
        if (content.isEmpty()) {
            return emptyPage(pageNumber, pageSize, (int) totalElements, totalPages);
        }
        return new Page(
                content,
                pageNumber,
                pageSize,
                content.size(),
                totalElements,
                totalPages,
                pageNumber == 1,
                pageNumber == totalPages
        );
    }

    public static <T> Page<T> emptyPage(int pageNumber, int pageSize, int totalElements, int totalPages) {
        return new Page(
                Collections.emptyList(),
                pageNumber,
                pageSize,
                0,
                totalElements,
                totalPages,
                false,
                false
        );
    }


    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getActualSize() {
        return actualSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean getIsFirstPage() {
        return isFirstPage;
    }

    public boolean getIsLastPage() {
        return isLastPage;
    }

}
