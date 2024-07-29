package codesqaud.app;

import jakarta.servlet.http.HttpServletRequest;

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

        String[] uriSegments = request.getRequestURI().split("/");

        if(this.uriSegments.length != uriSegments.length) {
            return false;
        }

        for (int i = 0; i < this.uriSegments.length; i++) {
            if(!this.uriSegments[i].equals("**") && !this.uriSegments[i].equals(uriSegments[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int compareTo(RequestMapper r) {
        int minLength = Math.min(uriSegments.length, r.uriSegments.length);
        for (int i = 0; i < minLength; i++) {
            if(this.uriSegments[i].equals(r.uriSegments[i])) {
                continue;
            }

            if(this.uriSegments[i].equals("**")) {
                return 1;
            }
            if(r.uriSegments[i].equals("**")) {
                return -1;
            }
        }

        return this.urlPattern.compareTo(r.urlPattern);
    }
}
