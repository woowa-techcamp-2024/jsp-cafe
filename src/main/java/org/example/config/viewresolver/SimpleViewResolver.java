package org.example.config.viewresolver;

import org.example.config.annotation.Component;

@Component
public class SimpleViewResolver implements ViewResolver {
    @Override
    public View getView(String viewName) {
        return new View("/WEB-INF/jsp/" + viewName + ".jsp");
    }
}
