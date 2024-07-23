package woowa.camp.jspcafe.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("")
public class HelloWorldServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("HelloWorldServlet doGet start");

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/index.jsp");
        requestDispatcher.forward(req, resp);

        log.debug("HelloWorldServlet doGet end");
    }

}
