package codesquad.container.initializer;

import jakarta.servlet.ServletContext;

public interface AppInit {
    void onStartUp(ServletContext servletContext) throws Exception;
}