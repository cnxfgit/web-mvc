package com.web.mvc.framework.annotation.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
    String bean();
    String msg() default "";
}
