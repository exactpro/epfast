package com.exactpro.epfast.annotation.processing.helpers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FieldSetterNameGenerator {
    private static Base64.Encoder encoder = Base64.getEncoder().withoutPadding();

    public static String generateClassName(String fastTypeName) {
        return "A" + encoder.encodeToString(fastTypeName.getBytes(StandardCharsets.UTF_8));
    }
}
