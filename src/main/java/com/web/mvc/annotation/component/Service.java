package com.web.mvc.annotation.component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface Service {
    String value() default "";
}
