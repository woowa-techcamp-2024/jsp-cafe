package com.woowa.framework.web;

import com.woowa.framework.Bean;
import com.woowa.framework.BeanFactory;
import com.woowa.framework.web.mapping.DynamicHandlerMapping;
import com.woowa.framework.web.mapping.StaticResourceHandlerMapping;

public class HandlerMappingConfig {

    @Bean
    public DynamicHandlerMapping dynamicHandlerMapping(BeanFactory beanFactory) {
        return new DynamicHandlerMapping(beanFactory);
    }

    @Bean
    public StaticResourceHandlerMapping staticResourceHandlerMapping() {
        return new StaticResourceHandlerMapping();
    }
}
