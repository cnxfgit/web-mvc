package com.web.mvc.framework.annotation.sql;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Insert {
    String value();
}
