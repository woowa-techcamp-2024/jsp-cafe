package org.example.cafe.common.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

public class Endpoint {

    protected final String method;
    protected final String path;

    public Endpoint(String method,
                    String path) {
        this.method = method;
        this.path = path;
    }

    public String method() {
        return method;
    }

    public String path() {
        return path;
    }

    public boolean match(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (this.path.endsWith("/") || this.path.endsWith("*")) {
            String comparePath = this.path.endsWith("*")
                    ? this.path.substring(0, this.path.length() - 1) : this.path;
            return path.startsWith(comparePath) && method.equals(this.method);
        }

        return this.path.equals(path) && this.method.equals(method);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Endpoint) obj;

        return Objects.equals(this.method, that.method) &&
                Objects.equals(this.path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }

    @Override
    public String toString() {
        return "Endpoint[" +
                "method=" + method + ", " +
                "path=" + path + ']';
    }
}