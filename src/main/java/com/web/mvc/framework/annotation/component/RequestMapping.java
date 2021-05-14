package com.web.mvc.framework.annotation.component;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();
}
