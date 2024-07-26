package org.example.config.mv;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private String viewPath;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void addAttribute(String key, Object value) {
        model.put(key, value);
    }

    public String getViewPath() {
        return viewPath;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
