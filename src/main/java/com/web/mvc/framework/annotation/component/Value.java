package com.web.mvc.framework.annotation.component;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {
    String value();
}
