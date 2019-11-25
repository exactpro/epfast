package com.exactpro.epfast.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface FastField {
    String name();
}
