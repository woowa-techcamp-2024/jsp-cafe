package org.example.jspcafe.common;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static org.example.jspcafe.common.StringUtils.isNumeric;

public class RequestUtil {
    public static Long extractLongPathVariable(HttpServletRequest req) throws IOException {
        String pathInfo = getPathInfo(req);

        String substring = pathInfo.substring(1);
        if (!isNumeric(substring)) {
            throw new IllegalArgumentException("Invalid pathInfo");
        }

        return Long.valueOf(substring);
    }

    public static String getPathInfo(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            throw new IllegalArgumentException("Invalid pathInfo");
        }
        return pathInfo;
    }
}
