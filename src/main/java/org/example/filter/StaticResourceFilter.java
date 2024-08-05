package org.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.service.ArticleService;
import org.example.util.LoggerUtil;
import org.slf4j.Logger;

@WebFilter(
    value = {"/*"},
    initParams = @WebInitParam(name = "encoding", value = "utf-8")
)
public class StaticResourceFilter implements Filter {

    private final Logger logger = LoggerUtil.getLogger();

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        logger.info("Request URI: {}", req.getRequestURI());
        // /user/form.html 요청을 /static/user/form.html로 리디렉션
        if (req.getRequestURI().equals("/user/form.html")) {
            res.sendRedirect("/static/user/form.html");
        } else if (req.getRequestURI().equals("/qna/form.html")) {
            res.sendRedirect("/static/qna/form.html");
        } else if (req.getRequestURI().equals("/error/not-same-author.html")) {
            res.sendRedirect("/static/error/not-same-author.html");
        }
        else if (req.getRequestURI().equals("/")) {
            ArticleService articleService = new ArticleService();
            int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
            int pageSize = req.getParameter("pageSize") == null ? 5 : Integer.parseInt(req.getParameter("pageSize"));
            req.setAttribute("articles", articleService.findAll(page, pageSize));
            req.setAttribute("page", page);
            req.setAttribute("size", pageSize);
            req.setAttribute("totalPage", articleService.getTotalPage(pageSize));
            req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, res);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}