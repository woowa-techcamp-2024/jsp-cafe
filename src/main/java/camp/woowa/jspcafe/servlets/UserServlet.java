package camp.woowa.jspcafe.servlets;

import camp.woowa.jspcafe.models.User;
import camp.woowa.jspcafe.services.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "userServlet", value = "/users")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        userService = (UserService) sc.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.findAll();
        req.setAttribute("users", users); // user 리스트 정보를 가져옴
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/user/list.jsp");

        try {
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            log(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        userService.createUser( req.getParameter("userId"),
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email"));
        try {
            res.sendRedirect("/users");
        } catch (IOException e) {
            log(e.getMessage());
        }
    }
}
