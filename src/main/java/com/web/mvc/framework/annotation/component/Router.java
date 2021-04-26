package com.web.mvc.framework.annotation.component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Router {
    String value() default "";
}
