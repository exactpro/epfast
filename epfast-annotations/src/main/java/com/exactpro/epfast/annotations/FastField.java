package com.exactpro.epfast.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface FastField {
    String name() default "";
}
