package cafe.users.servlet;

import cafe.MappingHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UserLogoutServlet extends MappingHttpServlet {
    @Override
    public List<String> mappings() {
        return List.of("/user/logout");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("user");
        resp.sendRedirect("/");
    }
}
