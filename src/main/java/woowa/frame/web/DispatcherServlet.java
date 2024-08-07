package woowa.frame.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.frame.core.BeanContainer;
import woowa.frame.core.annotation.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Component
public class DispatcherServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private BeanContainer beanContainer = BeanContainer.getInstance();
    private RouteTable table;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        table = beanContainer.getBean(RouteTable.class);
        JavaTimeModule module = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        objectMapper.registerModule(module);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Optional<RouteTableRow> routeTableRow = table.findTable(request);

        if (routeTableRow.isPresent()) {
            Object result = routeTableRow.get().handle(request, response);

            if (result == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (result instanceof String) {
                String stringResult = (String) result;
                if (stringResult.startsWith("redirect:")) {
                    response.sendRedirect(stringResult.substring(9));
                    return;
                }
                if (stringResult.endsWith(".jsp")) {
                    forward(request, response, stringResult);
                    return;
                }
            }

            if (result instanceof Integer) {
                response.setStatus((Integer) result);
                return;
            }

            if (result instanceof Map<?, ?>) {
                String json = objectMapper.writeValueAsString(result);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.print(json);
                writer.close();
            }

            forward(request, response, "/template/error/404.jsp");
        } else {
            forward(request, response, "/template/error/404.jsp");
        }
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
