package woowa.camp.jspcafe.web.mock;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

public class TestFilterChain implements FilterChain {

    private boolean filterCalled = false;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        filterCalled = true;
    }

    public boolean isFilterCalled() {
        return filterCalled;
    }

}
