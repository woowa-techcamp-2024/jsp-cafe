package org.example.filter;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryDBImpl;
import org.example.service.UserService;
import org.example.util.LoggerUtil;
import org.example.util.SessionUtil;
import org.slf4j.Logger;

@WebFilter(
    filterName = "AuthFilter",
    value = {"/*"}
)
public class AuthFilter implements Filter {
    private final Logger logger = LoggerUtil.getLogger();

    List<Pattern> checkPatterns = List.of(
        Pattern.compile("/question/\\d+")
    );

    List<String> exactPath = List.of(
        "/qna/form.html",
        "/static/qna/form.html"
    );

    private final UserRepository userRepository = UserRepositoryDBImpl.getInstance();

    @Override
    public void destroy() {	}
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        logger.info("AuthFilter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Optional<String> userId = SessionUtil.extractUserId(httpRequest);

        Optional<User> user = userId.map(userRepository::getUserByUserId).orElse(null);

        for(Pattern pattern : checkPatterns){
            if(pattern.matcher(httpRequest.getRequestURI()).matches()){
                if(userId.isEmpty() || user.isEmpty()){
                    httpRequest.getRequestDispatcher("/login").forward(request, response);
                    return;
                }
            }
        }
        for(String path : exactPath){
            if(httpRequest.getRequestURI().equals(path)){
                if(userId.isEmpty() || user.isEmpty()){
                    httpRequest.getRequestDispatcher("/login").forward(request, response);
                    return;
                }
            }
        }
        logger.info("session {}", httpRequest.getSession().getAttribute("userId"));

        chain.doFilter(request, response);
    }
    @Override
    public void init(FilterConfig filterConfig){
    }

}
