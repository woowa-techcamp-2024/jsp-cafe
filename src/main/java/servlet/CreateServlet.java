package servlet;

import domain.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;
import java.io.IOException;

@WebServlet(name = "servlet.CreateServlet", urlPatterns = {"/users"})
public class CreateServlet extends HttpServlet {

    public UserRepository userRepository;
    Logger log = LoggerFactory.getLogger(CreateServlet.class);

    @Override
    public void init(){
        log.info("Init CreateServlet");
        ServletContext context = getServletContext();
        this.userRepository = (UserRepository) context.getAttribute("repository");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Post /users");
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        if(userId.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
            return;
        }
        userRepository.saveUser(new User(userId, password, name, email));
        resp.sendRedirect("/users");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Get /users");
        req.setAttribute("users", userRepository.findAll());
        req.getRequestDispatcher("/user/list.jsp").forward(req, resp);
    }

}