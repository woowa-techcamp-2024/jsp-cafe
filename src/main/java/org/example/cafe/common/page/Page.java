package org.example.cafe.common.page;

import java.util.List;

public interface Page<T> {

    boolean hasPrevious();

    boolean hasNext();

    List<T> getContent();

    T getLastItem();

    int size();

    int getPageSize();
}
