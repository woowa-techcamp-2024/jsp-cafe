package codesqaud.app.exception;

public enum HttpStatus {
    BAD_REQUEST(400,"잘못된 요청입니다."),
    UNAUTHORIZED(401,"인증이 필요합니다."),
    FORBIDDEN(403,"접근이 금지되었습니다."),
    NOT_FOUND(404,"존재하지 않는 페이지 입니다."),
    METHOD_NOT_ALLOWED(405,"허용되지 않은 메소드 요청 입니다."),
    CONFLICT(409,"충돌이 발생했습니다."),


    INTERNAL_SERVER_ERROR(500,"서버 내부 오류입니다.");

    private final int code;
    private final String defaultMessage;

    HttpStatus(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public static HttpStatus valueOf(int statusCode) {
        for (HttpStatus status : values()) {
            if (status.code == statusCode) {
                return status;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
}
