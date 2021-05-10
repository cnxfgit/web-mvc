package com.web.mvc.framework.annotation.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
    Class bean();
    String value() default "";
}
