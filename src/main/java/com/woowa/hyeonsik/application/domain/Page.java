package com.woowa.hyeonsik.application.domain;

import java.util.Collection;
import java.util.Objects;

public final class Page<T> {
    private final long numberOfPage;
    private final long numberOfEnd;
    private final Collection<T> content;
    private final long totalCount;

    public Page(
            long numberOfPage,
            long numberOfEnd,
            Collection<T> content, long totalCount) {
        this.numberOfPage = numberOfPage;
        this.numberOfEnd = numberOfEnd;
        this.content = content;
        this.totalCount = totalCount;
    }

    public long getNumberOfPage() {
        return numberOfPage;
    }

    public long getNumberOfEnd() {
        return numberOfEnd;
    }

    public Collection<T> getContent() {
        return content;
    }

    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Page) obj;
        return this.numberOfPage == that.numberOfPage &&
                this.numberOfEnd == that.numberOfEnd &&
                Objects.equals(this.content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfPage, numberOfEnd, content);
    }

    @Override
    public String toString() {
        return "Page[" +
                "numberOfPage=" + numberOfPage + ", " +
                "numberOfEnd=" + numberOfEnd + ", " +
                "content=" + content + ']';
    }
}
