package codesqaud.app.filter;

import codesqaud.app.RequestMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebFilter("/*")
public class AuthenticationFilter extends HttpFilter {
    private final List<RequestMapper> permitAllRequests = new ArrayList<>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);

        List<RequestMapper> permittedRequests = List.of(
                RequestMapper.permittedGetMapping("/"),
                RequestMapper.permittedGetMapping("/users/login"),
                RequestMapper.permittedGetMapping("/users/form"),
                RequestMapper.permittedGetMapping("/qna/**"),

                RequestMapper.authenticatedGetMapping("/qna/form")
        );

        permitAllRequests.addAll(permittedRequests);

        Collections.sort(permitAllRequests);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            req.setAttribute("isLogin", true);
        } else {
            req.setAttribute("isLogin", false);
            checkAuthRequiredMapping(req, res);
        }

        if (res.getStatus() != HttpServletResponse.SC_UNAUTHORIZED) {
            super.doFilter(req, res, chain);
        }
    }

    private void checkAuthRequiredMapping(HttpServletRequest req, HttpServletResponse res) throws IOException {
        for (RequestMapper authRequiredResource : permitAllRequests) {
            if (authRequiredResource.matches(req)) {
                if(!authRequiredResource.permit()) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                return;
            }
        }
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
