package codesquad.global.handler;

import java.util.regex.Pattern;

public class HandlerMapping {
    private final Pattern pattern;
    private final RequestHandler handler;

    public HandlerMapping(Pattern pattern, RequestHandler handler) {
        this.pattern = pattern;
        this.handler = handler;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public RequestHandler getHandler() {
        return handler;
    }
}
