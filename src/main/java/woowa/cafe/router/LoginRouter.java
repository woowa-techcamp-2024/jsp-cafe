package woowa.cafe.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import woowa.cafe.dto.UserInfo;
import woowa.cafe.service.LoginService;
import woowa.frame.web.annotation.HttpMapping;
import woowa.frame.web.annotation.Router;

@Router
public class LoginRouter {

    private final LoginService loginService;

    public LoginRouter(LoginService loginService) {
        this.loginService = loginService;
    }

    @HttpMapping(method = "GET", urlTemplate = "/login")
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response) {
        return "/template/login/login.jsp";
    }

    @HttpMapping(method = "POST", urlTemplate = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        UserInfo userInfo = loginService.login(userId, password);
        if (userInfo != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userInfo", userInfo);
            session.setAttribute("userId", userId);
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    @HttpMapping(method = "GET", urlTemplate = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
