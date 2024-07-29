package camp.woowa.jspcafe.filter;

import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/qna/form","/qna/show/*"})
public class AuthenticateFilter extends HttpFilter {
    private static final String REDIRECT_LOGIN = "/user/login";
    private UserService userService;
    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        userService = (UserService) sc.getAttribute("userService");
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(REDIRECT_LOGIN);
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(REDIRECT_LOGIN);
            return;
        }

        if (!userService.validateAuthorization(user)) {
            response.sendRedirect(REDIRECT_LOGIN);
            return;
        }

        chain.doFilter(request, response);
    }
}
