package codesqaud.app;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 인가 처리를 위해 사용되는 클래스 입니다.
 * 인가처리가 필요한 요청에 대해 http 메소드와 URI 값을 인자로 전달하여 인스턴스를 생성합니다.
 * 해당 객체 인스턴스의 필드(method, urlPattern)와 request 객체의 필드(method, requestURI) 를 대조하여 요청이 인가처리가 필요한지 확인합니다.
 * urlPattern 에는 '**' 문자를 이용해서 와일드카드를 적용할 수 있습니다.
 *
 *
 * @param method 인가처리를 할 메소드입니다.
 * @param urlPattern 인가처리를 할 urlPattern 입니다.
 * @param uriSegments urlPattern 을 / 기준으로 split 한 배열입니다.
 * @param permit permit 이 true 면 모든 사용자의 요청을 허가합니다. permit 이 false 면 인증된 사용자에게만 요청을 허가합니다.
 */
public record RequestMapper(String method, String urlPattern, String[] uriSegments, boolean permit)
        implements Comparable<RequestMapper> {
    private static final String GET = "GET";
    private static final String POST = "POST";

    public static RequestMapper authenticatedGetMapping(String urlPattern) {
        return from(GET, urlPattern, false);
    }

    public static RequestMapper authenticatedPostMapping(String urlPattern) {
        return from(POST, urlPattern, false);
    }

    public static RequestMapper permittedGetMapping(String urlPattern) {
        return from(GET, urlPattern, true);
    }

    public static RequestMapper permittedPostMapping(String urlPattern) {
        return from(POST, urlPattern, true);
    }

    private static RequestMapper from(String method, String urlPattern, boolean permit) {
        return new RequestMapper(method, urlPattern, urlPattern.split("/"), permit);
    }

    public boolean matches(HttpServletRequest request) {
        if (!request.getMethod().equals(method)) {
            return false;
        }

        if(!containWildCard()) {
            return urlPattern.equals(request.getRequestURI());
        }

        String[] requestUriSegments = request.getRequestURI().split("/");

        int minLength = Math.min(this.uriSegments.length, requestUriSegments.length);
        for (int i = 0; i < minLength; i++) {
            if (this.uriSegments[i].equals("**")) {
                continue;
            }
            if (!this.uriSegments[i].equals(requestUriSegments[i])) {
                return false;
            }
        }

        if (isLastWildCard()) {
            return this.uriSegments.length <= requestUriSegments.length;
        }

        return this.uriSegments.length == requestUriSegments.length;
    }

    private boolean isLastWildCard() {
        int urlLength = this.uriSegments.length;
        return urlLength > 0 && this.uriSegments[urlLength - 1].equals("**");
    }

    private boolean containWildCard() {
        for (String uriSegment : uriSegments) {
            if(uriSegment.equals("**")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(RequestMapper r) {
        int minLength = Math.min(uriSegments.length, r.uriSegments.length);
        for (int i = 0; i < minLength; i++) {
            if (this.uriSegments[i].equals(r.uriSegments[i])) {
                continue;
            }

            if (this.uriSegments[i].equals("**")) {
                return 1;
            }
            if (r.uriSegments[i].equals("**")) {
                return -1;
            }
        }

        return r.urlPattern.compareTo(this.urlPattern);
    }
}
