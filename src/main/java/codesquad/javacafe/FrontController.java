package codesquad.javacafe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import codesquad.javacafe.member.controller.MemberController;
import codesquad.javacafe.common.SubController;
import codesquad.javacafe.member.controller.MemberInfoController;
import codesquad.javacafe.member.controller.MemberProfileController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/api/*")
public class FrontController extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private static Map<String, SubController> subControllers = new HashMap<>();
    // TODO proxy controller 만들기?
    private String message;

    public void init() {
        log.info("[Initializing FrontController]");
        subControllers.put("/users", new MemberController());
        subControllers.put("/users/profile", new MemberProfileController());
        subControllers.put("/users/info", new MemberInfoController());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) {
        log.info("[FrontController] service, Request URI = {}, Http Method = {}",req.getRequestURI(),req.getMethod());
        var uri = req.getRequestURI().replace("/api","");
        log.debug("[FrontController] uri = {}", uri);
        var subController = subControllers.get(uri);
        // TODO 없는 요청 404 처리
        log.debug(subController.toString());
        try {
            subController.doProcess(req, res);
        } catch (ServletException exception) {

        } catch (IOException exception) {

        }
    }

    public void destroy() {
    }
}