package camp.woowa.jspcafe.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "userServlet", value = "/users")
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        userService.createUser( req.getParameter("userId"),
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email"));
        try {
            res.sendRedirect("/user/list.html");
        } catch (IOException e) {
            log(e.getMessage());
        }
    }
}
