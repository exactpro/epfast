package com.exactpro.epfast.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface FastType {
    String name() default "";
}
