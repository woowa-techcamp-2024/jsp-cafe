package woowa.frame.web.collection;

import java.util.List;

public class Page<T> {
    private final List<T> content;
    private final int size;
    private final long totalElements;

    public Page(List<T> content, int size, long totalElements) {
        this.content = content;
        this.size = size;
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public long getTotalPages() {
        return (long) Math.ceil((double) totalElements / size);
    }
}
