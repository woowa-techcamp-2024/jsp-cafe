package codesqaud.app.servlet;

import codesqaud.app.dao.UserDao;
import codesqaud.app.model.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "registrationServlet", value = "/users")
public class UserServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.userDao = (UserDao) config.getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        User user = new User(userId, password, name, email);
        userDao.save(user);
        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.setHeader("Location","/users");
    }
}
