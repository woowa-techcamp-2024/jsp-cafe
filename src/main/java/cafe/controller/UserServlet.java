package cafe.controller;

import cafe.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "userServlet", value = "/users/*")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String uri = req.getRequestURI();

            if (uri.equals("/users")) doGetUsers(req, resp);
            else {
                String[] uriParts = uri.split("/");
                if (uriParts[uriParts.length-1].equals("form")) doGetUser(req, resp, "/user/update_form.jsp");
                else doGetUser(req, resp, "/user/profile.jsp");
            }
        } catch (IOException | ServletException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String uri = req.getRequestURI();

            if (uri.equals("/users")) userService.save(req, resp);
            else userService.update(req, resp);
            resp.sendRedirect("/users");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void doGetUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("users", userService.findAll(req, resp));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/user/list.jsp");
        dispatcher.forward(req, resp);
    }

    private void doGetUser(HttpServletRequest req, HttpServletResponse resp, String url) throws IOException, ServletException {
        req.setAttribute("user", userService.find(req, resp));
        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);
    }
}
