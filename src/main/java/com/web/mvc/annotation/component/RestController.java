package com.web.mvc.annotation.component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
    String value() default "";
}
