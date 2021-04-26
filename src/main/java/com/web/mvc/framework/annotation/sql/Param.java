package com.web.mvc.framework.annotation.sql;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value();
}
