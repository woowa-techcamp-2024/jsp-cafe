package org.example.cafe.common.endpoint;

import jakarta.servlet.http.HttpServletRequest;

public class PathEndPoint extends Endpoint {

    private final String query;

    public PathEndPoint(String method, String path, String query) {
        super(method, path);
        this.query = query;
    }

    @Override
    public boolean match(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String query = request.getQueryString();

        if (this.path.endsWith("/") || this.path.endsWith("*")) {
            String comparePath = this.path.endsWith("*")
                    ? this.path.substring(0, this.path.length() - 1) : this.path;
            return path.startsWith(comparePath) && this.method.equals(method) && this.query.equals(query);
        }

        return this.path.equals(path) && this.method.equals(method) && this.query.equals(query);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "PathEndPoint[]";
    }
}