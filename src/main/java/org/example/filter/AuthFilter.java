package org.example.filter;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import org.example.util.LoggerUtil;
import org.slf4j.Logger;

@WebFilter(
    filterName = "AuthFilter",
    value = {"/*"}
)
public class AuthFilter implements Filter {
    private final Logger logger = LoggerUtil.getLogger();

    List<Pattern> checkPatterns = List.of(
        Pattern.compile("/question/\\d+")
    );

    List<String> exactPath = List.of(
        "/qna/form.html",
        "static/qna/form.html"
    );

    @Override
    public void destroy() {	}
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        logger.info("AuthFilter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        for(Pattern pattern : checkPatterns){
            if(pattern.matcher(httpRequest.getRequestURI()).matches()){
                if(httpRequest.getSession().getAttribute("userId") == null){
                    httpRequest.getRequestDispatcher("/login").forward(request, response);
                    return;
                }
            }
        }
        for(String path : exactPath){
            if(httpRequest.getRequestURI().equals(path)){
                if(httpRequest.getSession().getAttribute("userId") == null){
                    httpRequest.getRequestDispatcher("/login").forward(request, response);
                    return;
                }
            }
        }
        logger.info("session {}", httpRequest.getSession().getAttribute("userId"));

        chain.doFilter(request, response);
    }
    @Override
    public void init(FilterConfig filterConfig){
    }

}
