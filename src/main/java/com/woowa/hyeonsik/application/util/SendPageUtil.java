package com.woowa.hyeonsik.application.util;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendPageUtil {
    private static final Logger logger = LoggerFactory.getLogger(SendPageUtil.class);
    private SendPageUtil() {}

    /**
     * 페이지를 Forward 합니다.
     * @param path 서버 기준 Path입니다. 현재 프로젝트의 webapp을 기준으로 경로를 지정합니다.
     * @param app ServletContext입니다.
     * @param request HttpServletRequest입니다.
     * @param response HttpServletResponse입니다.
     */
    public static void forward(String path, ServletContext app, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        validatePath(path, app.getContextPath());
        RequestDispatcher dispatcher = app.getRequestDispatcher(path);

        dispatcher.forward(request, response);
    }

    /**
     * 페이지를 Redirect 합니다.
     * @param path 클라이언트 기준 Path입니다. 외부 접속 기준으로 경로를 지정합니다. Context Path는 내부적으로 설정됩니다.
     * @param app ServletContext입니다.
     * @param response HttpServletResponse입니다.
     */
    public static void redirect(String path, ServletContext app, HttpServletResponse response) throws IOException {
        final String contextPath = app.getContextPath();
        validatePath(path, contextPath);

        response.sendRedirect(contextPath + path);
    }

    private static void validatePath(String path, String contextPath) {
        if (path == null) {
            throw new NullPointerException("Path값은 Null일 수 없습니다.");
        }
        if (path.startsWith(contextPath)) {
            logger.info("ContextPath가 이미 경로에 포함되어 있는 것 같습니다.");
        }
    }
}
