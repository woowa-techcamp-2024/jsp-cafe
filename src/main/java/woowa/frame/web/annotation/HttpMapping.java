package woowa.frame.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HTTP API와 연결을 나타내는 어노테이션입니다. @Router가 선언된 클래스에서 동작합니다.
 *
 * @see Router
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpMapping {

    /**
     * http method <br>
     * GET, POST, PUT, DELETE
     * @return
     */
    String method();

    String urlTemplate() default "";
}
