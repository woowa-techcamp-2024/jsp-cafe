package codesquad.common.handler.annotation;

import codesquad.common.handler.ReturnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Response {
    ReturnType returnType() default ReturnType.HTML;
}
