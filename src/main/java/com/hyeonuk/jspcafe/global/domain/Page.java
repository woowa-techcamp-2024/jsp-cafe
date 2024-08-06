package com.hyeonuk.jspcafe.global.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Page<T> {
    private final int size;
    private final int page;
    private final int totalPage;
    private final boolean previousPage;
    private final boolean nextPage;
    private final List<T> contents;

    public Page(int size, int page, int totalPage, List<T> contents) {
        this.size = size;
        this.page = page;
        this.totalPage = totalPage;
        this.previousPage = page > 0;
        this.nextPage = page < totalPage;
        this.contents = contents;
    }

    public List<Integer> pageList(){
        int minPage = (page % size) * size;
        int maxPage = Math.min(minPage + size, totalPage);

        return IntStream.range(minPage, maxPage)
                .map(p->p+1)
                .boxed()
                .collect(Collectors.toList());
    }

    public List<T> getContents(){
        return contents;
    }

    public int getSize() {
        return size;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public boolean isPreviousPage() {
        return previousPage;
    }

    public boolean isNextPage() {
        return nextPage;
    }
}
