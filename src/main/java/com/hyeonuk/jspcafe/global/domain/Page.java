package com.hyeonuk.jspcafe.global.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Page<T> {
    private final int size;
    private final int page;
    private final long totalSize;
    private final boolean previousPage;
    private final boolean nextPage;
    private final List<T> contents;
    private final long totalPage;
    private final int listSize;

    public Page(int size, int page, long totalSize, List<T> contents) {
        this.size = size;
        this.page = page;
        this.totalSize = totalSize;
        this.totalPage = totalSize/size;
        this.previousPage = page > 1;
        this.nextPage = page < totalSize/size;
        this.contents = contents;
        this.listSize = 10;
    }

    public List<Long> pageList(){
        long minPage = page /listSize*listSize;
        long maxPage = Math.min(minPage + listSize, totalPage);
        return LongStream.range(minPage, maxPage)
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

    public long getTotalSize() {
        return totalSize;
    }

    public boolean isPreviousPage() {
        return previousPage;
    }

    public boolean isNextPage() {
        return nextPage;
    }
}