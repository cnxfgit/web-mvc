package com.web.mvc.annotation.param;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.PARAMETER)
public @interface RequestBody {
    String value() default "";
}
