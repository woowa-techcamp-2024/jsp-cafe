package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.exception.InternalServerError;
import org.example.demo.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
    private final Logger logger = LoggerFactory.getLogger(Router.class);
    private List<Route> routes = new ArrayList<>();

    public void addRoute(HttpMethod method, String urlPattern, RouteHandler handler) {
        routes.add(new Route(method, urlPattern, handler));
    }

    public boolean route(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        // form 이 POST, GET 만 지원하기 때문에 _hidden 으로 오는 필드를 통해 처리
        if (request.getParameter("_method") != null) {
            method = HttpMethod.valueOf(request.getParameter("_method"));
        }

        logger.info("method: {}, path: {}", method, path);

        for (Route route : routes) {
            if (route.method == method) {
                Matcher matcher = Pattern.compile(route.urlPattern).matcher(path);
                if (matcher.matches()) {
                    List<String> params = new ArrayList<>();
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        params.add(matcher.group(i));
                    }
                    try {
                        route.handler.handle(request, response, params);
                    } catch (IOException | ServletException e) {
                        throw new InternalServerError(e.getMessage());
                    } catch (UnauthorizedException e) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static class Route {
        HttpMethod method;
        String urlPattern;
        RouteHandler handler;

        Route(HttpMethod method, String urlPattern, RouteHandler handler) {
            this.method = method;
            this.urlPattern = urlPattern;
            this.handler = handler;
        }
    }
}
