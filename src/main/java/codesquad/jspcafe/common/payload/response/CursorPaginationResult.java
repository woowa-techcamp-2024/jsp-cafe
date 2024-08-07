package codesquad.jspcafe.common.payload.response;

import java.util.List;

public class CursorPaginationResult<T> {

    private final List<T> data;
    private final boolean hasNext;
    private final Integer numberOfElements;

    private CursorPaginationResult(List<T> data, boolean hasNext, Integer numberOfElements) {
        this.data = data;
        this.hasNext = hasNext;
        this.numberOfElements = numberOfElements;
    }

    public static <T> CursorPaginationResult<T> of(List<T> data, int requestSize) {
        List<T> subList = data.subList(0, Math.min(requestSize, data.size()));
        return new CursorPaginationResult<>(subList, data.size() > requestSize, subList.size());
    }

    public List<T> getData() {
        return data;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }
}
