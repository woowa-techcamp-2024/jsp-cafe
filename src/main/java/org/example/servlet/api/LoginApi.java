package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.config.DataHandler;
import org.example.data.UserDataHandler;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/api/login")
public class LoginApi extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(LoginApi.class);
    private UserDataHandler userDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDataHandler = (UserDataHandler) config.getServletContext().getAttribute(DataHandler.USER.getValue());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = userDataHandler.findByEmail(email);
        if (isUserNull(request, response, user)) {
            return;
        }
        if (isInvalidPassword(request, response, user.getPassword(), password)) {
            return;
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect("/");
    }

    private boolean isUserNull(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        if (user == null) {
            request.setAttribute("status_code", HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("message", "User 가 없습니다.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }

    private boolean isInvalidPassword(HttpServletRequest request, HttpServletResponse response, String userPassword,
                                      String inputPassword) throws ServletException, IOException {
        if (!userPassword.equals(inputPassword)) {
            request.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("message", "비밀번호가 맞지 않습니다");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }
}
