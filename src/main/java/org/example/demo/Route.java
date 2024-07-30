package org.example.demo;

public class Route {
    HttpMethod method;
    String urlPattern;
    RouteHandler handler;

    Route(HttpMethod method, String urlPattern, RouteHandler handler) {
        this.method = method;
        this.urlPattern = urlPattern;
        this.handler = handler;
    }
}
