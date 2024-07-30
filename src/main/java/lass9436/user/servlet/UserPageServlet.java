package lass9436.user.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/userPage")
public class UserPageServlet extends HttpServlet {

    private Map<String, BiFunction<HttpServletRequest, HttpServletResponse, String>> actionMethodMap;
    private final String path = "/WEB-INF/user";

    @Override
    public void init() {
        actionMethodMap = new HashMap<>();
        actionMethodMap.put("register", this::handleRegister);
        actionMethodMap.put("login-failed", this::handleLoginFailed);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            BiFunction<HttpServletRequest, HttpServletResponse, String> actionMethod = getHandler(action);
            String viewPath = actionMethod.apply(req, resp);
            req.getRequestDispatcher(viewPath).forward(req, resp);
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
        return path + "/register.jsp";
    }

    private String handleLoginFailed(HttpServletRequest req, HttpServletResponse resp) {
        return path + "/loginFailed.jsp";
    }
}
