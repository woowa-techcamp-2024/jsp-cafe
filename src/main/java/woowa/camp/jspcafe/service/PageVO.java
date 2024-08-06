package woowa.camp.jspcafe.service;

public class PageVO {

    public static final int MAX_ROW_COUNT_PER_PAGE = 15;    // 한 페이지에 보여지는 Row 최대 개수
    public static final int MAX_PAGE_COUNT = 10;   // 페이지 버튼의 최대 개수

    private final long currentPage;
    private long firstPage;
    private long lastPage;
    private long totalPageCount;

    private boolean previousPageExist;
    private boolean nextPageExist;

    public PageVO(long currentPage, long totalRowCount) {
        this.currentPage = currentPage;
        setTotalPageCount(totalRowCount);
        setFirstPage();
        setLastPage();
        setPreviousPageExist();
        setNextPageExist();
    }

    private void setTotalPageCount(long totalRowCount) {
        totalPageCount = (totalRowCount + MAX_ROW_COUNT_PER_PAGE - 1) / MAX_ROW_COUNT_PER_PAGE;
    }

    private void setFirstPage() {
        firstPage = (currentPage - 1) / MAX_PAGE_COUNT * MAX_PAGE_COUNT + 1;
    }

    private void setLastPage() {
        lastPage = Math.min(totalPageCount, firstPage + MAX_PAGE_COUNT - 1);
    }

    private void setPreviousPageExist() {
        previousPageExist = firstPage > 1L;
    }

    private void setNextPageExist() {
        nextPageExist = lastPage < totalPageCount;
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
