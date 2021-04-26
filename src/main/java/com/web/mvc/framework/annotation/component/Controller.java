package com.web.mvc.framework.annotation.component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String value() default "";
}
