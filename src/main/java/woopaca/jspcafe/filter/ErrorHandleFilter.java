package woopaca.jspcafe.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woopaca.jspcafe.error.ClientErrorException;

import java.io.IOException;

@WebFilter("*")
public class ErrorHandleFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(ErrorHandleFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        try {
            chain.doFilter(request, response);
        } catch (ClientErrorException e) {
            log.warn("클라이언트 오류", e);
            httpRequest.setAttribute("errorMessage", e.getMessage());
            httpResponse.sendError(e.getStatus().getCode(), e.getMessage());
        }
    }
}
