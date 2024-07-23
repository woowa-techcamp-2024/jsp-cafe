package codesquad.jspcafe.domain.user.controller;

import codesquad.jspcafe.domain.user.payload.response.UserCommonResponse;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/*")
public class UserInfoServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid userId");
            return;
        }
        String userId = pathInfo.substring(1);
        UserCommonResponse userCommonResponse = userService.getUserById(userId);
        req.setAttribute("user", userCommonResponse);
        req.getRequestDispatcher("/resources/jsp/profile.jsp").forward(req, resp);
    }
}
