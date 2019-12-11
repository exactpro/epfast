package com.exactpro.junit5;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(WithByteBufExtension.class)
public @interface WithByteBuf {
    String value();
}
