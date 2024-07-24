package com.woowa.framework;

import java.util.ArrayList;
import java.util.List;

public class ApplicationInitializer implements Initializer {

    private final List<Initializer> initializers = new ArrayList<>();

    public ApplicationInitializer(BeanFactory beanFactory) {
        initializers.addAll(beanFactory.getBeans(Initializer.class));
    }

    @Override
    public void init() {
        for (Initializer bean : initializers) {
            bean.init();
        }
    }
}
