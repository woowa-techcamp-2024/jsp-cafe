package codesqaud.app.dto;

import java.util.List;

public class PageDto<T> {
    private List<T> elements;
    private long totalElementsCount;
    private long totalPage;
    private boolean hasNext;
    private boolean hasPrevious;

    public PageDto(List<T> elements, boolean hasNext, long totalElementsCount) {
        this.elements = elements;
        this.hasNext = hasNext;
        this.totalElementsCount = totalElementsCount;
    }

    public PageDto(List<T> elements, boolean hasNext, boolean hasPrevious, long totalElementsCount, long totalPage) {
        this.elements = elements;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.totalElementsCount = totalElementsCount;
        this.totalPage = totalPage;
    }

    public List<T> getElements() {
        return elements;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public long getTotalElementsCount() {
        return totalElementsCount;
    }

    public long getTotalPage() {
        return totalPage;
    }
}
