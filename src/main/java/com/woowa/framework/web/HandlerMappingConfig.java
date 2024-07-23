package com.woowa.framework.web;

import com.woowa.framework.Bean;
import com.woowa.framework.BeanFactory;

public class HandlerMappingConfig {

    @Bean
    public DynamicHandlerMapping dynamicHandlerMapping(BeanFactory beanFactory) {
        DynamicHandlerMapping dynamicHandlerMapping = new DynamicHandlerMapping(beanFactory);
        dynamicHandlerMapping.init();
        return dynamicHandlerMapping;
    }

    @Bean
    public StaticResourceHandlerMapping staticResourceHandlerMapping() {
        return new StaticResourceHandlerMapping();
    }
}
