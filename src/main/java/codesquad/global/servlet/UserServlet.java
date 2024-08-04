package codesquad.global.servlet;

import codesquad.common.handler.HandlerMapping;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/users", "/users/*"})
public class UserServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.info("UserServlet initializing...");
        ServletContext servletContext = config.getServletContext();
        handlerMappings = (List<HandlerMapping>) servletContext.getAttribute("HandlerMappings");
        logger.info("UserServlet initialized. Mapped for {}", handlerMappings);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("UserServlet service");
        String requestURI = req.getRequestURI();
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.matches(requestURI)) {
                handlerMapping.handle(req, resp);
                return;
            }
        }
        logger.info("UserServlet no match");
        String errMsg = "{0} {1}같은 페이지 없어요.";
        errMsg = MessageFormat.format(errMsg, req.getRequestURI(), req.getMethod());
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, errMsg);
    }
}
