package codesquad.container;

import jakarta.servlet.ServletContext;

public class ServletRegister implements AppInit {
    @Override
    public void onStartUp(ServletContext servletContext) {
        servletContext.addServlet("defaultJsp", "org.apache.jasper.servlet.JspServlet").addMapping("/");
    }
}
