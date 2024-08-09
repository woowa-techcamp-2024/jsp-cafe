package woowa.camp.jspcafe.service;

public class PageVO {

    public static final int MAX_ROW_COUNT_PER_PAGE = 15;    // 한 페이지에 보여지는 Row 최대 개수
    public static final int MAX_PAGE_COUNT = 10;   // 페이지 버튼의 최대 개수

    private final long currentPage;
    private final long firstPage;
    private final long lastPage;
    private final long totalPageCount;

    private final boolean previousPageExist;
    private final boolean nextPageExist;

    public PageVO(long currentPage, long totalRowCount) {
        this.currentPage = currentPage;
        this.totalPageCount = calculateTotalPageCount(totalRowCount);
        this.firstPage = calculateFirstPage();
        this.lastPage = calculateLastPage();
        this.previousPageExist = calculatePreviousPageExist();
        this.nextPageExist = calculateNextPageExist();
    }

    private long calculateTotalPageCount(long totalRowCount) {
        return (totalRowCount + MAX_ROW_COUNT_PER_PAGE - 1) / MAX_ROW_COUNT_PER_PAGE;
    }

    private long calculateFirstPage() {
        return (currentPage - 1) / MAX_PAGE_COUNT * MAX_PAGE_COUNT + 1;
    }

    private long calculateLastPage() {
        return Math.min(totalPageCount, firstPage + MAX_PAGE_COUNT - 1);
    }

    private boolean calculatePreviousPageExist() {
        return firstPage > 1L;
    }

    private boolean calculateNextPageExist() {
        return lastPage < totalPageCount;
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

    public boolean isNextPageExist() {
        return nextPageExist;
    }

    public boolean isPreviousPageExist() {
        return previousPageExist;
    }

    @Override
    public String toString() {
        return "PageVO{" +
                "currentPage=" + currentPage +
                ", firstPage=" + firstPage +
                ", lastPage=" + lastPage +
                ", totalPageCount=" + totalPageCount +
                ", previousPageExist=" + previousPageExist +
                ", nextPageExist=" + nextPageExist +
                '}';
    }
}
