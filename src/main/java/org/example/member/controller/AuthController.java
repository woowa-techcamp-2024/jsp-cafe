package org.example.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import org.example.config.HttpMethod;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Controller;
import org.example.config.annotation.RequestMapping;
import org.example.config.annotation.RequestParam;
import org.example.config.mv.ModelAndView;
import org.example.member.service.UserService;
import org.example.util.session.InMemorySessionManager;
import org.example.util.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path = "/user")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final SessionManager sessionManager = InMemorySessionManager.getInstance();

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/login", method = HttpMethod.GET)
    public ModelAndView getLoginPage() {
        return new ModelAndView("redirect:/static/user/login.html");
    }

    @RequestMapping(path = "/login", method = HttpMethod.POST)
    public ModelAndView login(@RequestParam("userId") String userId, @RequestParam("password") String password,
                              HttpServletRequest request)
            throws SQLException {
        logger.info("로그인 시도 : {}", userId);
        try {
            if (userService.validateUser(userId, password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", userId);
                sessionManager.addSessionToManager(session);
                return new ModelAndView("redirect:/");
            }
            return new ModelAndView("redirect:/user/login_failed.html");
        } catch (SQLException e) {
            return new ModelAndView("redirect:/user/login_failed.html");
        }
    }

    @RequestMapping(path = "/logout", method = HttpMethod.GET)
    public ModelAndView logout(HttpServletRequest request) throws IOException {
        String sessionId = request.getSession().getId();
        sessionManager.invalidateSession(sessionId);
        return new ModelAndView("redirect:/");
    }
}
