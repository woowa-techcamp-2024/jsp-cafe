package codesqaud.app.servlet;


import codesqaud.app.exception.HttpException;
import codesqaud.app.exception.HttpStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/error")
public class ErrorHandlingServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandlingServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleException(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleException(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleException(req, resp);
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Throwable throwable = (Throwable) req.getAttribute("jakarta.servlet.error.exception");
        Integer statusCode = (Integer) req.getAttribute("jakarta.servlet.error.status_code");
        String errorMessage = null;

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if(throwable != null) {
            log.error(throwable.getMessage(), throwable);
        }

        if (throwable instanceof HttpException httpException) {
            httpStatus = HttpStatus.valueOf(httpException.getStatusCode());
            statusCode = httpException.getStatusCode();
            errorMessage = httpException.getMessage();
        } else if (statusCode != null) {
            httpStatus = HttpStatus.valueOf(statusCode);
            statusCode = httpStatus.getCode();
            errorMessage = httpStatus.getDefaultMessage();
        }

        if (statusCode == null) {
            statusCode = httpStatus.getCode();
        }
        if (errorMessage == null) {
            errorMessage = httpStatus.getDefaultMessage();
        }

        if(statusCode == HttpServletResponse.SC_UNAUTHORIZED) {
            resp.sendRedirect("/users/login");
            return;
        }

        req.setAttribute("errorMessage", errorMessage);
        req.setAttribute("statusCode", statusCode);
        req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
    }
}
