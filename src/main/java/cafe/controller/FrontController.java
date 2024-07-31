package cafe.controller;

import cafe.controller.handler.Handler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController extends HttpServlet {
    private final Map<String, Handler> handlers = new HashMap<>();

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        handlers.put("default", (Handler) servletContext.getAttribute("defaultHandler"));
        handlers.put("userInfo", (Handler) servletContext.getAttribute("userInfoHandler"));
        handlers.put("userInfoList", (Handler) servletContext.getAttribute("userInfoListHandler"));
        handlers.put("userSignUp", (Handler) servletContext.getAttribute("userSignUpHandler"));
        handlers.put("userInfoEdit", (Handler) servletContext.getAttribute("userInfoEditHandler"));
        handlers.put("userSignIn", (Handler) servletContext.getAttribute("userSignInHandler"));
        handlers.put("userSignOut", (Handler) servletContext.getAttribute("userSignOutHandler"));
        handlers.put("article", (Handler) servletContext.getAttribute("articleHandler"));
        handlers.put("articleList", (Handler) servletContext.getAttribute("articleListHandler"));
        handlers.put("articleCreate", (Handler) servletContext.getAttribute("articleCreateHandler"));
        handlers.put("articleUpdate", (Handler) servletContext.getAttribute("articleUpdateHandler"));
        handlers.put("articleDelete", (Handler) servletContext.getAttribute("articleDeleteHandler"));
        handlers.put("commentList", (Handler) servletContext.getAttribute("commentListHandler"));
        handlers.put("commentCreate", (Handler) servletContext.getAttribute("commentCreateHandler"));
        handlers.put("commentDelete", (Handler) servletContext.getAttribute("commentDeleteHandler"));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uri = req.getRequestURI();
        if (uri.equals("/")) {
            this.doDefaultService(req, resp);
            return;
        }

        String firstUri = uri.split("/")[1];
        switch (firstUri) {
            case "users": {
                this.doUserService(req, resp);
                return;
            }
            case "articles": {
                this.doArticleService(req, resp);
                return;
            }
            case "comments": {
                this.doCommentService(req, resp);
                return;
            }
            default: this.doDefaultService(req, resp);
        }
    }

    private void doDefaultService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handlers.get("default").handle(req, resp);
    }

    private void doUserService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/users")) {
            handlers.get("userInfoList").handle(req, resp);
            return;
        }

        String[] uriParts = uri.split("/");
        switch (uriParts[2]) {
            case "sign-up": {
                handlers.get("userSignUp").handle(req, resp);
                return;
            }
            case "sign-in": {
                handlers.get("userSignIn").handle(req, resp);
                return;
            }
            case "sign-out": {
                handlers.get("userSignOut").handle(req, resp);
                return;
            }
            default: {
                if (uriParts.length == 3) {
                    handlers.get("userInfo").handle(req, resp);
                } else if (uriParts[3].equals("edit")) {
                    handlers.get("userInfoEdit").handle(req, resp);
                }
            }
        }
    }

    private void doArticleService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.equals("/articles")) {
            handlers.get("articleList").handle(req, resp);
            return;
        }

        String[] uriParts = uri.split("/");
        switch (uriParts[2]) {
            case "create": {
                handlers.get("articleCreate").handle(req, resp);
                return;
            }
            default: {
                if (uriParts.length == 3) {
                    handlers.get("article").handle(req, resp);
                }
                else {
                    if (uriParts[3].equals("update")) handlers.get("articleUpdate").handle(req, resp);
                    else if (uriParts[3].equals("delete")) handlers.get("articleDelete").handle(req, resp);
                }
            }
        }
    }

    private void doCommentService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] uriParts = uri.split("/");

        if (uriParts.length == 3) handlers.get("commentList").handle(req, resp);
        else if (uriParts[3].equals("create")) handlers.get("commentCreate").handle(req, resp);
        else if (uriParts[3].equals("delete")) handlers.get("commentDelete").handle(req, resp);
    }
}
