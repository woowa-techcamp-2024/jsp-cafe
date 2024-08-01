package com.codesquad.cafe.util;

import com.codesquad.cafe.exception.ResourceNotFoundException;

public final class PathVariableUtil {
    private PathVariableUtil() {
    }

    public static Long parsePathVariable(String pathInfo) {
        try {
            String[] paths = pathInfo.substring(1).split("/");
            return Long.parseLong(paths[0]);
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException();
        }
    }

}
