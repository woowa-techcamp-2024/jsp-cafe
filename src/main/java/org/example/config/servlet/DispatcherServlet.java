package org.example.config.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map.Entry;
import org.example.config.HttpMethod;
import org.example.config.conatiner.ApplicationContext;
import org.example.config.controller.MethodHandler;
import org.example.config.handler.AnnotationHandlerMapping.HandlerKey;
import org.example.config.handler.HandlerMapping;
import org.example.config.invoker.ControllerMethodInvoker;
import org.example.config.mv.ModelAndView;
import org.example.config.viewresolver.View;
import org.example.config.viewresolver.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private HandlerMapping handlerMapping;
    private ViewResolver viewResolver;
    private ControllerMethodInvoker methodInvoker;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute("applicationContext");

        if (applicationContext == null) {
            throw new ServletException("ApplicationContext not found in ServletContext");
        }

        this.handlerMapping = applicationContext.getBean(HandlerMapping.class);
        this.viewResolver = applicationContext.getBean(ViewResolver.class);
        this.methodInvoker = new ControllerMethodInvoker();

        logger.info("handler mapping: {}", handlerMapping);
        logger.info("view resolver: {}", viewResolver);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isStaticResource(req)) {
            getServletContext().getNamedDispatcher("default").forward(req, resp);
            return;
        }
        req.getParameterNames().asIterator().forEachRemaining(name -> logger.info("param name: {}", name));

        String requestURI = req.getRequestURI();
        HttpMethod httpMethod = HttpMethod.valueOf(req.getMethod());

        try {
            Entry<HandlerKey, MethodHandler> handlerEntry = handlerMapping.getHandler(requestURI, httpMethod);
            MethodHandler handler = handlerEntry.getValue();
            req.setAttribute("currentUrlPattern", handlerEntry.getKey().getUrl());
            if (handler == null) {
                logger.warn("No handler found for request: {} {}", httpMethod, requestURI);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Object result = methodInvoker.invokeHandlerMethod(handler, req, resp);
            if (result instanceof ModelAndView) {
                ModelAndView mv = (ModelAndView) result;
                String viewPath = mv.getViewPath();
                if (viewPath.startsWith("redirect:")) {
                    resp.sendRedirect(viewPath.substring(9));
                } else {
                    View view = viewResolver.getView(viewPath);
                    view.render(mv.getModel(), req, resp);
                }
            }

        } catch (IllegalArgumentException e) {
            logger.error("Error processing request", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            logger.error("Error processing request", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error processing request", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isStaticResource(HttpServletRequest req) {
        String requestURI = req.getRequestURI();

        String[] staticExtensions = {".css", ".js", ".jpg", ".jpeg", ".png", ".gif", ".ico", ".html", ".htm"};

        String[] staticDirectories = {"/static/", "/resources/", "/assets/", "/fonts/"};

        for (String extension : staticExtensions) {
            if (requestURI.toLowerCase().endsWith(extension)) {
                return true;
            }
        }

        for (String directory : staticDirectories) {
            if (requestURI.startsWith(req.getContextPath() + directory)) {
                return true;
            }
        }

        return false;
    }
}
