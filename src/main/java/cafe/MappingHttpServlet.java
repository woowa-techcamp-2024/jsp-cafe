package cafe;

import jakarta.servlet.http.HttpServlet;

import java.util.List;

public abstract class MappingHttpServlet extends HttpServlet {

    public abstract List<String> mappings();
}
