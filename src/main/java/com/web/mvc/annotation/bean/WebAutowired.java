package com.web.mvc.annotation.bean;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface WebAutowired {
    String value() default "";
}
