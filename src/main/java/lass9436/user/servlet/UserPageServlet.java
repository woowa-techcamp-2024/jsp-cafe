package lass9436.user.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.user.model.UserRepository;

@WebServlet("/userPage")
public class UserPageServlet extends HttpServlet {

    private UserRepository userRepository;
    private Map<String, BiFunction<HttpServletRequest, HttpServletResponse, String>> actionMethodMap;
    private final String path = "/WEB-INF/user";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userRepository = (UserRepository)config.getServletContext().getAttribute("userRepository");
        actionMethodMap = new HashMap<>();
        actionMethodMap.put("register", this::handleRegister);
        actionMethodMap.put("login-failed", this::handleLoginFailed);
        actionMethodMap.put("list", this::handleList);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            BiFunction<HttpServletRequest, HttpServletResponse, String> actionMethod = getHandler(action);
            String viewPath = actionMethod.apply(req, resp);
            req.getRequestDispatcher(path + viewPath).forward(req, resp);
        } catch (RuntimeException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    private BiFunction<HttpServletRequest, HttpServletResponse, String> getHandler(String action){
        if (action == null || action.isEmpty()) throw new IllegalArgumentException("action is null or empty");
        if (!actionMethodMap.containsKey(action)) throw new IllegalArgumentException("unknown action: " + action);
        return actionMethodMap.get(action);
    }

    private String handleRegister(HttpServletRequest req, HttpServletResponse resp) {
        return "/register.jsp";
    }

    private String handleLoginFailed(HttpServletRequest req, HttpServletResponse resp) {
        return "/loginFailed.jsp";
    }

    private String handleList(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("users", userRepository.findAll());
        return "/list.jsp";
    }
}
