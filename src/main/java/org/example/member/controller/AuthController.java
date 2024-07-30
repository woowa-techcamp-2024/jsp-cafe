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
import org.example.member.model.dao.User;
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
        return new ModelAndView("/user/UserLogin");
    }

    @RequestMapping(path = "/login", method = HttpMethod.POST)
    public ModelAndView login(@RequestParam("userId") String userId, @RequestParam("password") String password,
                              HttpServletRequest request) {
        logger.info("로그인 시도 : {}", userId);
        ModelAndView mv = new ModelAndView("/user/UserLogin");
        try {
            if (userService.validateUser(userId, password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", userId);
                sessionManager.addSessionToManager(session);
                return new ModelAndView("redirect:/");
            } else {
                mv.addAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
            }
        } catch (SQLException e) {
            logger.error("로그인 처리 중 오류 발생", e);
            mv.addAttribute("loginError", "로그인 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
        return mv;
    }

    @RequestMapping(path = "/logout", method = HttpMethod.GET)
    public ModelAndView logout(HttpServletRequest request) throws IOException {
        String sessionId = request.getSession().getId();
        sessionManager.invalidateSession(sessionId);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = "/signup", method = HttpMethod.GET)
    public ModelAndView userRegisterFrom() {
        return new ModelAndView("user/UserSignup");
    }

    @RequestMapping(path = "/signup", method = HttpMethod.POST)
    public ModelAndView registerUser(@RequestParam("userId") String userId,
                                     @RequestParam("password") String password,
                                     @RequestParam("name") String name,
                                     @RequestParam("email") String email) throws SQLException {
        ModelAndView mv = new ModelAndView("redirect:/users");

        User user = User.createUser(userId, password, name, email);
        userService.register(user);
        return mv;
    }
}
