package camp.woowa.jspcafe.db.page;

import camp.woowa.jspcafe.model.Question;

import java.util.List;

public class Page<T> {
    private int currentPage;
    private int totalPage;
    private List<T> contents;

    public Page(List<T> contents, int totalPage, int currentPage) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.contents = contents;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<T> getContents() {
        return contents;
    }
}
