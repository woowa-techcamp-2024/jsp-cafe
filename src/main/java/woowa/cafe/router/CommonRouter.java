package woowa.cafe.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woowa.frame.web.annotation.HttpMapping;
import woowa.frame.web.annotation.Router;

@Router
public class CommonRouter {

    @HttpMapping(method = "GET", urlTemplate = "/header")
    public String header(HttpServletRequest request, HttpServletResponse response) {
        return "template/common/header.jsp";
    }
}
