package com.wootecam.jspcafe.exception;

import jakarta.servlet.http.HttpServletResponse;

public class InternalServerException extends CommonException {

    private static final String MESSAGE = "서버에서 요청을 처리할 수 없습니다.";

    public InternalServerException() {
        super(MESSAGE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
