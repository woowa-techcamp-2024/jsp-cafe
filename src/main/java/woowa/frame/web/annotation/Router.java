package woowa.frame.web.annotation;

import woowa.frame.core.annotation.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Router {

    /**
     * 라우터에 적용하는 최상위 path를 나타냅니다.
     */
    String url() default "";
}
