package camp.woowa.jspcafe.servlet;

import camp.woowa.jspcafe.exception.CustomException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/error")
public class GlobalExceptionServlet extends HttpServlet {
    private static final String EXCEPTION_ATTRIBUTE = "jakarta.servlet.error.exception";
    private static final String CUSTOM_EXCEPTION_ATTRIBUTE = "camp.woowa.jspcafe.exception.CustomException";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp) {
        Throwable throwable = (Throwable) req.getAttribute(EXCEPTION_ATTRIBUTE);
        CustomException exception;

        if  (throwable instanceof CustomException) {
            exception = (CustomException) throwable;
        } else {
            int statusCode = resp.getStatus();
            String message = throwable != null ? throwable.getMessage() : "알 수 없는 오류입니다.";
            exception = new CustomException(statusCode, message, "관리자에게 문의하십시오");
        }

        req.setAttribute(CUSTOM_EXCEPTION_ATTRIBUTE, exception);

        try {
            req.getRequestDispatcher("/WEB-INF/jsp/error/error.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log("Failed to forward request", e);

            // 만약의 경우를 위한 대비책
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
