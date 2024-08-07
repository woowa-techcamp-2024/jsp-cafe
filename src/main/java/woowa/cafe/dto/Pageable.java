package woowa.cafe.dto;

public record Pageable(
        int page,
        int size,
        String sort
) {
    public int getOffset() {
        return (page - 1) * size;
    }

    public int getSize() {
        return size;
    }
}
