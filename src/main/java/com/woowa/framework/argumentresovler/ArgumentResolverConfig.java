package com.woowa.framework.argumentresovler;

import com.woowa.framework.Bean;

public class ArgumentResolverConfig {

    @Bean
    public ArgumentResolverComposite defaultArgumentResolver() {
        ArgumentResolverComposite argumentResolverComposite = new ArgumentResolverComposite();
        argumentResolverComposite.addArgumentResolver(new HttpServletRequestResolver());
        argumentResolverComposite.addArgumentResolver(new HttpServletResponseResolver());
        argumentResolverComposite.addArgumentResolver(new RequestParameterResolver());
        return argumentResolverComposite;
    }
}
