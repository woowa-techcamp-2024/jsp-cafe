package woowa.camp.jspcafe.service;

public class PageVO {

    public static final int MAX_ROW_COUNT_PER_PAGE = 15;    // 한 페이지에 보여지는 Row 최대 개수
    public static final int MAX_PAGE_COUNT = 10;   // 페이지 버튼의 최대 개수

    private final long currentPage;
    private long firstPage;
    private long lastPage;

    public PageVO(long currentPage, long totalRowCount) {
        this.currentPage = currentPage;
        setFirstPage();
        setLastPage(totalRowCount);
    }

    private void setFirstPage() {
        firstPage = (currentPage - 1) / MAX_PAGE_COUNT * MAX_PAGE_COUNT + 1;
    }

    private void setLastPage(long totalRowCount) {
        long totalPageCount = totalRowCount / MAX_ROW_COUNT_PER_PAGE + 1;
        lastPage = Math.min(totalPageCount, firstPage + MAX_PAGE_COUNT - 1);
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public long getFirstPage() {
        return firstPage;
    }

    public long getLastPage() {
        return lastPage;
    }

    @Override
    public String toString() {
        return "Page{" +
                "curPage=" + currentPage +
                ", firstPage=" + firstPage +
                ", lastPage=" + lastPage +
                '}';
    }
}
