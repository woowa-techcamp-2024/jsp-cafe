package camp.woowa.jspcafe.db.page;

public class PageRequest {
    private int page;
    private int size;
    private int offset; // offset = (page - 1) * size

    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
        this.offset = (page - 1) * size;
    }

    public int getPage() {
        return page;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }
}
