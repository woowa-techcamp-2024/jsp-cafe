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
import java.util.Optional;

@WebServlet("/users/*")
public class ProfileServlet extends HttpServlet {

    public UserRepository userRepository;
    Logger log = LoggerFactory.getLogger(CreateServlet.class);

    @Override
    public void init(){
        log.info("Init ProfileServlet");
        ServletContext context = getServletContext();
        this.userRepository = (UserRepository) context.getAttribute("repository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long userId = Long.parseLong(pathParts[1]);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
            return;
        }

        req.setAttribute("user", user.get());
        req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
    }

}
