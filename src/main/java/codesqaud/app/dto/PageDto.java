package codesqaud.app.dto;

import java.util.List;

public class PageDto<T> {
    private List<T> elements;
    private long totalElementsCount;
    private long totalPage;
    private boolean hasNext;
    private boolean hasPrevious;

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

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private List<T> elements;
        private long totalElementsCount;
        private long totalPage;
        private boolean hasNext;
        private boolean hasPrevious;

        private Builder() {
        }

        public Builder<T> elements(List<T> elements) {
            this.elements = elements;
            return this;
        }

        public Builder<T> totalElementsCount(long totalElementsCount) {
            this.totalElementsCount = totalElementsCount;
            return this;
        }

        public Builder<T> totalPage(long totalPage) {
            this.totalPage = totalPage;
            return this;
        }

        public Builder<T> hasNext(boolean hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public Builder<T> hasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
            return this;
        }

        public PageDto<T> build() {
            return new PageDto<>(elements, hasNext, hasPrevious, totalElementsCount, totalPage);
        }
    }
}
