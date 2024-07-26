package org.example.config.viewresolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class View {

    private String viewPath;
    private Map<String, Object> model = new HashMap<>();

    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        modelToAttribute(model, request);
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    public View(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getViewName() {
        return viewPath;
    }

    public void setViewName(String viewName) {
        this.viewPath = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    private void modelToAttribute(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((key, value) -> {
            request.setAttribute(key, value);
        });
    }
}
