package com.web.mvc.annotation.param;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.PARAMETER)
public @interface WebRequestBody {
    String value() default "";
}