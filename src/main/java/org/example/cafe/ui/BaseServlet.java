package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.cafe.common.error.CafeException;
import org.slf4j.Logger;

public class BaseServlet extends HttpServlet {

    private static final Logger log = getLogger(BaseServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            super.service(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            if ("GET".equals(request.getMethod()) || "POST".equals(request.getMethod())) {
                request.setAttribute("errorMessage", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
                return;
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonError = "서비스에 장애가 발생했습니다.";
            if (e instanceof CafeException ce) {
                response.setStatus(ce.getStatusCode());
                jsonError = "{\"error\": \"" + e.getMessage() + "\"}";
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            response.getWriter().write(jsonError);
        }
    }
}
