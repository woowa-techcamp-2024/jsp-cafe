package org.example.cafe.common.page;

import java.util.List;

public class CursorPage<T> implements Page<T> {

    private final List<T> content;
    private final int pageSize;
    private final boolean hasNext;

    public CursorPage(List<T> content, int pageSize) {
        if (content.size() > pageSize) {
            hasNext = true;
            content.remove(content.size() - 1);
        } else {
            hasNext = false;
        }
        this.content = content;
        this.pageSize = pageSize;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public T getLastItem() {
        if (content.isEmpty()) {
            return null;
        }
        return content.get(content.size() - 1);
    }

    @Override
    public int size() {
        return content.size();
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }
}
